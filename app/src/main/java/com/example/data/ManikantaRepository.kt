package com.example.data

import android.content.Context
import android.util.Log
import com.example.R
import com.example.model.*
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ManikantaRepository(
    private val context: Context,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    private val tag = "ManikantaRepo"

    val db: FirebaseFirestore by lazy {
        try {
            val dbId = context.getString(R.string.firestore_database_id)
            FirebaseFirestore.getInstance(dbId)
        } catch (e: Exception) {
            Log.w(tag, "Could not initialize named database ID, fallback default", e)
            FirebaseFirestore.getInstance()
        }
    }

    private val auth = Firebase.auth

    // Role state
    private val _currentRole = MutableStateFlow(AppRole.CUSTOMER)
    val currentRole: StateFlow<AppRole> = _currentRole.asStateFlow()

    // Domain collections
    private val _products = MutableStateFlow<List<Product>>(ManikantaInitialData.defaultProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(ManikantaInitialData.defaultCategories)
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _banners = MutableStateFlow<List<BannerItem>>(ManikantaInitialData.defaultBanners)
    val banners: StateFlow<List<BannerItem>> = _banners.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _inventoryLogs = MutableStateFlow<List<InventoryLog>>(emptyList())
    val inventoryLogs: StateFlow<List<InventoryLog>> = _inventoryLogs.asStateFlow()

    private val _businessProfile = MutableStateFlow(BusinessProfile())
    val businessProfile: StateFlow<BusinessProfile> = _businessProfile.asStateFlow()

    private val _customerCareTickets = MutableStateFlow<List<CustomerCareTicket>>(emptyList())
    val customerCareTickets: StateFlow<List<CustomerCareTicket>> = _customerCareTickets.asStateFlow()

    private val _staffMembers = MutableStateFlow(
        listOf(
            StaffMember(
                id = "staff_01",
                name = "Kishore Kumar",
                email = "kishore@manikantanaturals.in",
                phone = "+91 98480 55441",
                role = "STORE_MANAGER",
                active = true
            ),
            StaffMember(
                id = "staff_02",
                name = "Sujatha Rao",
                email = "sujatha@manikantanaturals.in",
                phone = "+91 98480 88772",
                role = "INVENTORY_LEAD",
                active = true
            )
        )
    )
    val staffMembers: StateFlow<List<StaffMember>> = _staffMembers.asStateFlow()

    // Cart and Wishlist
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _wishlistIds = MutableStateFlow<Set<String>>(emptySet())
    val wishlistIds: StateFlow<Set<String>> = _wishlistIds.asStateFlow()

    private val _isInitialSyncComplete = MutableStateFlow(false)
    val isInitialSyncComplete: StateFlow<Boolean> = _isInitialSyncComplete.asStateFlow()

    init {
        startRealtimeListeners()
    }

    fun setAppRole(role: AppRole) {
        _currentRole.value = role
    }

    private fun startRealtimeListeners() {
        scope.launch {
            try {
                // Products Listener
                db.collection("products").addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e(tag, "Products listener error: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            val list = snapshot.toObjects(Product::class.java)
                            _products.value = list
                        } else {
                            // Seed once if completely empty on brand new project
                            seedInitialCatalogIfEmpty()
                        }
                    }
                }

                // Categories Listener
                db.collection("categories").addSnapshotListener { snapshot, error ->
                    if (error == null && snapshot != null && !snapshot.isEmpty) {
                        _categories.value = snapshot.toObjects(Category::class.java)
                    }
                }

                // Banners Listener
                db.collection("banners").addSnapshotListener { snapshot, error ->
                    if (error == null && snapshot != null && !snapshot.isEmpty) {
                        _banners.value = snapshot.toObjects(BannerItem::class.java)
                    }
                }

                // Orders Listener
                db.collection("orders").addSnapshotListener { snapshot, error ->
                    if (error == null && snapshot != null) {
                        _orders.value = snapshot.toObjects(Order::class.java)
                            .sortedByDescending { it.createdAt?.seconds ?: 0 }
                    }
                }

                // Inventory Logs Listener
                db.collection("inventory_logs").addSnapshotListener { snapshot, error ->
                    if (error == null && snapshot != null) {
                        _inventoryLogs.value = snapshot.toObjects(InventoryLog::class.java)
                            .sortedByDescending { it.timestamp?.seconds ?: 0 }
                    }
                }

                // Profile Listener
                db.collection("business_profile").document("manikanta_main").addSnapshotListener { doc, error ->
                    if (error == null && doc != null && doc.exists()) {
                        doc.toObject(BusinessProfile::class.java)?.let { _businessProfile.value = it }
                    }
                }

                // Customer Care Listener
                db.collection("customer_care").addSnapshotListener { snapshot, error ->
                    if (error == null && snapshot != null) {
                        _customerCareTickets.value = snapshot.toObjects(CustomerCareTicket::class.java)
                    }
                }

                _isInitialSyncComplete.value = true
            } catch (e: Exception) {
                Log.w(tag, "Realtime sync error: ${e.message}")
            }
        }
    }

    private fun seedInitialCatalogIfEmpty() {
        scope.launch {
            try {
                val batch = db.batch()
                ManikantaInitialData.defaultProducts.forEach { product ->
                    val ref = db.collection("products").document(product.id)
                    batch.set(ref, product)
                }
                ManikantaInitialData.defaultCategories.forEach { category ->
                    val ref = db.collection("categories").document(category.id)
                    batch.set(ref, category)
                }
                ManikantaInitialData.defaultBanners.forEach { banner ->
                    val ref = db.collection("banners").document(banner.id)
                    batch.set(ref, banner)
                }
                val profileRef = db.collection("business_profile").document("manikanta_main")
                batch.set(profileRef, BusinessProfile())
                batch.commit().await()
                Log.i(tag, "Successfully seeded initial catalog into persistent Cloud Firestore")
            } catch (e: Exception) {
                Log.w(tag, "Seeding check completed: ${e.message}")
            }
        }
    }

    // ==========================================
    // CRITICAL ATOMIC TRANSACTION: PLACE ORDER
    // ==========================================
    suspend fun placeOrderWithAtomicStockDeduction(
        customerName: String,
        customerPhone: String,
        shippingAddress: String,
        paymentMethod: String = "UPI",
        notes: String = ""
    ): Result<Order> {
        val currentCart = _cart.value
        if (currentCart.isEmpty()) {
            return Result.failure(IllegalStateException("Cart is empty"))
        }

        val currentUserId = auth.currentUser?.uid ?: "guest_cust_${UUID.randomUUID().toString().take(6)}"
        val orderId = "ORD-" + UUID.randomUUID().toString().take(8).uppercase()

        return try {
            val createdOrder = db.runTransaction { transaction ->
                val orderItems = mutableListOf<OrderItem>()
                var total = 0.0

                // 1. Verify and deduct stock atomically for all cart items
                for (cartItem in currentCart) {
                    val productRef = db.collection("products").document(cartItem.product.id)
                    val snapshot = transaction.get(productRef)
                    if (!snapshot.exists()) {
                        throw IllegalStateException("Product ${cartItem.product.name} no longer exists")
                    }

                    val currentStock = (snapshot.getLong("availableStock") ?: 0L).toInt()
                    if (currentStock < cartItem.quantity) {
                        throw IllegalStateException(
                            "Insufficient stock for '${cartItem.product.name}'. Available: $currentStock, Requested: ${cartItem.quantity}"
                        )
                    }

                    val newStock = currentStock - cartItem.quantity
                    transaction.update(productRef, "availableStock", newStock)
                    transaction.update(productRef, "updatedAt", Timestamp.now())

                    // Log inventory reduction
                    val logRef = db.collection("inventory_logs").document()
                    val logEntry = InventoryLog(
                        id = logRef.id,
                        productId = cartItem.product.id,
                        productName = cartItem.product.name,
                        sku = cartItem.product.sku,
                        previousStock = currentStock,
                        newStock = newStock,
                        quantityDelta = -cartItem.quantity,
                        reason = "ORDER_FULFILLMENT ($orderId)",
                        actorId = currentUserId,
                        timestamp = Timestamp.now()
                    )
                    transaction.set(logRef, logEntry)

                    val itemSubtotal = cartItem.product.effectivePrice * cartItem.quantity
                    total += itemSubtotal
                    orderItems.add(
                        OrderItem(
                            productId = cartItem.product.id,
                            productName = cartItem.product.name,
                            sku = cartItem.product.sku,
                            price = cartItem.product.effectivePrice,
                            quantity = cartItem.quantity,
                            unit = cartItem.product.unit,
                            subtotal = itemSubtotal
                        )
                    )
                }

                // 2. Create the immutable historical Order record
                val order = Order(
                    id = orderId,
                    customerId = currentUserId,
                    customerName = customerName,
                    customerPhone = customerPhone,
                    shippingAddress = shippingAddress,
                    items = orderItems,
                    totalAmount = total,
                    paymentMethod = paymentMethod,
                    paymentStatus = PaymentStatus.PAID.name,
                    orderStatus = OrderStatus.CONFIRMED.name,
                    trackingNumber = "MN-TRK-" + UUID.randomUUID().toString().take(6).uppercase(),
                    notes = notes,
                    createdAt = Timestamp.now(),
                    updatedAt = Timestamp.now()
                )

                val orderRef = db.collection("orders").document(orderId)
                transaction.set(orderRef, order)
                order
            }.await()

            // Clear local cart upon successful atomic transaction
            _cart.value = emptyList()
            Result.success(createdOrder)
        } catch (e: Exception) {
            Log.e(tag, "Order atomic transaction failed", e)
            Result.failure(e)
        }
    }

    // ==========================================
    // PRODUCT & INVENTORY MANAGEMENT (OWNER)
    // ==========================================
    suspend fun saveProduct(product: Product): Result<String> {
        return try {
            val docId = product.id.ifEmpty { "prod_" + UUID.randomUUID().toString().take(8) }
            val toSave = product.copy(
                id = docId,
                updatedAt = Timestamp.now(),
                createdAt = product.createdAt ?: Timestamp.now()
            )
            db.collection("products").document(docId).set(toSave).await()
            _products.update { current ->
                val existing = current.indexOfFirst { it.id == docId }
                if (existing >= 0) {
                    current.toMutableList().apply { set(existing, toSave) }
                } else {
                    listOf(toSave) + current
                }
            }
            Result.success(docId)
        } catch (e: Exception) {
            Log.e(tag, "Failed to save product", e)
            Result.failure(e)
        }
    }

    suspend fun updateStock(productId: String, newStock: Int, reason: String): Result<Unit> {
        require(newStock >= 0) { "Stock quantity cannot be negative" }
        return try {
            db.runTransaction { transaction ->
                val ref = db.collection("products").document(productId)
                val snap = transaction.get(ref)
                val prevStock = (snap.getLong("availableStock") ?: 0L).toInt()
                val delta = newStock - prevStock
                val productName = snap.getString("name") ?: "Product"
                val sku = snap.getString("sku") ?: ""

                transaction.update(ref, "availableStock", newStock)
                transaction.update(ref, "updatedAt", Timestamp.now())

                val logRef = db.collection("inventory_logs").document()
                val log = InventoryLog(
                    id = logRef.id,
                    productId = productId,
                    productName = productName,
                    sku = sku,
                    previousStock = prevStock,
                    newStock = newStock,
                    quantityDelta = delta,
                    reason = reason,
                    actorId = auth.currentUser?.email ?: "Owner",
                    timestamp = Timestamp.now()
                )
                transaction.set(logRef, log)
            }.await()

            _products.update { current ->
                current.map { if (it.id == productId) it.copy(availableStock = newStock) else it }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(tag, "Failed to update stock", e)
            Result.failure(e)
        }
    }

    suspend fun archiveProduct(productId: String): Result<Unit> {
        return try {
            val ref = db.collection("products").document(productId)
            ref.update(
                mapOf(
                    "status" to ProductStatus.ARCHIVED.name,
                    "updatedAt" to Timestamp.now()
                )
            ).await()

            _products.update { current ->
                current.map {
                    if (it.id == productId) it.copy(status = ProductStatus.ARCHIVED.name) else it
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(tag, "Failed to archive product", e)
            Result.failure(e)
        }
    }

    suspend fun saveCategory(category: Category): Result<Unit> {
        return try {
            val docId = category.id.ifEmpty { "cat_" + UUID.randomUUID().toString().take(8) }
            val toSave = category.copy(id = docId)
            db.collection("categories").document(docId).set(toSave).await()
            _categories.update { current ->
                val idx = current.indexOfFirst { it.id == docId }
                if (idx >= 0) current.toMutableList().apply { set(idx, toSave) } else current + toSave
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String, trackingNumber: String? = null): Result<Unit> {
        return try {
            val ref = db.collection("orders").document(orderId)
            val updates = mutableMapOf<String, Any>(
                "orderStatus" to newStatus,
                "updatedAt" to Timestamp.now()
            )
            if (!trackingNumber.isNullOrEmpty()) {
                updates["trackingNumber"] = trackingNumber
            }
            ref.update(updates).await()
            _orders.update { current ->
                current.map { if (it.id == orderId) it.copy(orderStatus = newStatus) else it }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBusinessProfile(profile: BusinessProfile): Result<Unit> {
        return try {
            db.collection("business_profile").document("manikanta_main").set(profile).await()
            _businessProfile.value = profile
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addBanner(banner: BannerItem): Result<Unit> {
        return try {
            val docId = banner.id.ifEmpty { "ban_" + UUID.randomUUID().toString().take(6) }
            val toSave = banner.copy(id = docId)
            db.collection("banners").document(docId).set(toSave).await()
            _banners.update { current -> listOf(toSave) + current }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addCustomerCareTicket(name: String, phone: String, message: String): Result<Unit> {
        return try {
            val docId = "tkt_" + UUID.randomUUID().toString().take(6)
            val ticket = CustomerCareTicket(
                id = docId,
                customerId = auth.currentUser?.uid ?: "guest",
                customerName = name,
                customerPhone = phone,
                message = message,
                status = "OPEN",
                createdAt = Timestamp.now()
            )
            db.collection("customer_care").document(docId).set(ticket).await()
            _customerCareTickets.update { listOf(ticket) + it }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun replyCustomerCareTicket(ticketId: String, response: String): Result<Unit> {
        return try {
            db.collection("customer_care").document(ticketId).update(
                mapOf("response" to response, "status" to "RESOLVED")
            ).await()
            _customerCareTickets.update { current ->
                current.map { if (it.id == ticketId) it.copy(response = response, status = "RESOLVED") else it }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addStaffMember(staff: StaffMember): Result<Unit> {
        val docId = staff.id.ifEmpty { "staff_" + UUID.randomUUID().toString().take(6) }
        val toSave = staff.copy(id = docId)
        _staffMembers.update { it + toSave }
        return Result.success(Unit)
    }

    // ==========================================
    // CART & WISHLIST HELPERS
    // ==========================================
    fun addToCart(product: Product, quantity: Int = 1) {
        _cart.update { current ->
            val existing = current.find { it.product.id == product.id }
            if (existing != null) {
                current.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + quantity) else it
                }
            } else {
                current + CartItem(product = product, quantity = quantity)
            }
        }
    }

    fun updateCartQuantity(productId: String, delta: Int) {
        _cart.update { current ->
            current.mapNotNull {
                if (it.product.id == productId) {
                    val newQty = it.quantity + delta
                    if (newQty > 0) it.copy(quantity = newQty) else null
                } else it
            }
        }
    }

    fun removeFromCart(productId: String) {
        _cart.update { current -> current.filterNot { it.product.id == productId } }
    }

    fun toggleWishlist(productId: String) {
        _wishlistIds.update { current ->
            if (current.contains(productId)) current - productId else current + productId
        }
    }
}
