package com.example.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class ProductStatus {
    PUBLISHED,
    ARCHIVED
}

enum class OrderStatus {
    PLACED,
    CONFIRMED,
    DISPATCHED,
    DELIVERED,
    CANCELLED
}

enum class PaymentStatus {
    PENDING,
    PAID,
    FAILED
}

enum class AppRole {
    CUSTOMER,
    BUSINESS_OWNER,
    DEVELOPER_SUPER_ADMIN
}

@IgnoreExtraProperties
data class Product(
    @DocumentId val id: String = "",
    val name: String = "",
    val description: String = "",
    val categoryId: String = "",
    val categoryName: String = "",
    val sku: String = "",
    val price: Double = 0.0,
    val offerPrice: Double = 0.0,
    val availableStock: Int = 0,
    val unit: String = "500 ml", // e.g. 500 ml, 1 L, 1 kg, 500 g
    val imageUrl: String = "",
    val imageStoragePath: String = "",
    val status: String = ProductStatus.PUBLISHED.name,
    val isFeatured: Boolean = false,
    val isNewArrival: Boolean = false,
    val storeId: String = "manikanta_main",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null,
    val updatedBy: String = "Owner"
) {
    val effectivePrice: Double get() = if (offerPrice > 0.0 && offerPrice < price) offerPrice else price
    val isArchived: Boolean get() = status == ProductStatus.ARCHIVED.name
    val inStock: Boolean get() = availableStock > 0
}

@IgnoreExtraProperties
data class Category(
    @DocumentId val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val sortOrder: Int = 0,
    val active: Boolean = true
)

@IgnoreExtraProperties
data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val sku: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val unit: String = "500 ml",
    val subtotal: Double = 0.0
)

@IgnoreExtraProperties
data class Order(
    @DocumentId val id: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val shippingAddress: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val paymentMethod: String = "UPI",
    val paymentStatus: String = PaymentStatus.PENDING.name,
    val orderStatus: String = OrderStatus.PLACED.name,
    val trackingNumber: String = "",
    val notes: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class InventoryLog(
    @DocumentId val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val sku: String = "",
    val previousStock: Int = 0,
    val newStock: Int = 0,
    val quantityDelta: Int = 0,
    val reason: String = "STOCK_UPDATE", // RESTOCK, ORDER_FULFILLMENT, DAMAGE, MANUAL_ADJUSTMENT
    val actorId: String = "Owner",
    @ServerTimestamp val timestamp: Timestamp? = null
)

@IgnoreExtraProperties
data class BannerItem(
    @DocumentId val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val imageUrl: String = "",
    val targetCategoryId: String = "",
    val active: Boolean = true,
    val sortOrder: Int = 0
)

@IgnoreExtraProperties
data class BusinessProfile(
    @DocumentId val storeId: String = "manikanta_main",
    val storeName: String = "Manikanta Naturals",
    val tagline: String = "100% Pure, Wood-Pressed & Natural Farm Wellness",
    val logoUrl: String = "",
    val founderName: String = "Manikanta Reddy",
    val founderMessage: String = "At Manikanta Naturals, we are committed to bringing the wisdom of traditional wood-pressed extraction, unpasteurized forest honey, and unadulterated farm produce directly to your family's table.",
    val founderPhotoUrl: String = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600&q=80",
    val address: String = "Plot No. 42, Natural Agro Park, Organic Corridor, Hyderabad, Telangana - 500081",
    val phone: String = "+91 98480 12345",
    val email: String = "care@manikantanaturals.in",
    val whatsappNumber: String = "+919848012345",
    val gstNumber: String = "36AAECM1234F1Z8",
    val googleAdsTrackingId: String = "AW-981240182",
    val metaPixelId: String = "PIXEL-77120491",
    val googleAdsEnabled: Boolean = true,
    val metaAdsEnabled: Boolean = true
)

@IgnoreExtraProperties
data class CustomerCareTicket(
    @DocumentId val id: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val message: String = "",
    val response: String = "",
    val status: String = "OPEN", // OPEN, RESOLVED
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class StaffMember(
    @DocumentId val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "STORE_MANAGER", // STORE_MANAGER, INVENTORY_LEAD, DISPATCHER
    val active: Boolean = true,
    val assignedPermissions: List<String> = listOf("MANAGE_PRODUCTS", "MANAGE_ORDERS", "MANAGE_STOCK")
)

data class CartItem(
    val product: Product,
    val quantity: Int
)
