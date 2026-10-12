package com.example.data

import com.example.model.*

object ManikantaInitialData {

    val defaultCategories = listOf(
        Category(
            id = "cat_wood_pressed_oils",
            name = "Wood-Pressed Oils",
            description = "Cold-pressed in traditional wooden churners (Vaagai Chekku) below 40°C without chemicals.",
            imageUrl = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=600&q=80",
            sortOrder = 1,
            active = true
        ),
        Category(
            id = "cat_raw_honey",
            name = "Wild Forest Honey",
            description = "Unheated, unfiltered raw honey harvested ethically from deep tribal forest reserves.",
            imageUrl = "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=600&q=80",
            sortOrder = 2,
            active = true
        ),
        Category(
            id = "cat_desi_ghee",
            name = "A2 Desi Cow Ghee",
            description = "Bilona method churned from grass-fed indigenous Gir cow curd.",
            imageUrl = "https://images.unsplash.com/photo-1628088062854-d1870b4553da?w=600&q=80",
            sortOrder = 3,
            active = true
        ),
        Category(
            id = "cat_ayurvedic_herbs",
            name = "Ayurvedic Powders",
            description = "Sun-dried farm-grown herbal roots and medicinal leaves.",
            imageUrl = "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=600&q=80",
            sortOrder = 4,
            active = true
        ),
        Category(
            id = "cat_pure_spices",
            name = "Farm Spices & Turmeric",
            description = "High curcumin single-origin spices stone-pounded for maximum aroma.",
            imageUrl = "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=600&q=80",
            sortOrder = 5,
            active = true
        ),
        Category(
            id = "cat_natural_skincare",
            name = "Natural Skin & Body",
            description = "Chemical-free cold process handmade soaps and herbal body care.",
            imageUrl = "https://images.unsplash.com/photo-1608248597359-5452377bcece?w=600&q=80",
            sortOrder = 6,
            active = true
        )
    )

    val defaultProducts = listOf(
        Product(
            id = "prod_groundnut_oil_1l",
            name = "Wood-Pressed Groundnut Oil (Cold Pressed)",
            description = "Extracted from premium sun-dried Saurashtra groundnuts using slow traditional wooden churners. Retains natural vitamin E, heart-healthy sterols, and deep nutty aroma. Zero heat, zero preservatives.",
            categoryId = "cat_wood_pressed_oils",
            categoryName = "Wood-Pressed Oils",
            sku = "MN-OIL-GN-1000",
            price = 380.0,
            offerPrice = 340.0,
            availableStock = 85,
            unit = "1 Litre",
            imageUrl = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=600&q=80",
            status = ProductStatus.PUBLISHED.name,
            isFeatured = true,
            isNewArrival = false
        ),
        Product(
            id = "prod_sesame_oil_500ml",
            name = "Wood-Pressed Black Sesame Gingelly Oil",
            description = "Cold extracted with palm jaggery in traditional stone-and-wood pestles. Ideal for traditional cooking, oil pulling, and holistic Ayurvedic massage.",
            categoryId = "cat_wood_pressed_oils",
            categoryName = "Wood-Pressed Oils",
            sku = "MN-OIL-SES-500",
            price = 290.0,
            offerPrice = 260.0,
            availableStock = 50,
            unit = "500 ml",
            imageUrl = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=600&q=80",
            status = ProductStatus.PUBLISHED.name,
            isFeatured = false,
            isNewArrival = true
        ),
        Product(
            id = "prod_forest_honey_500g",
            name = "Raw Unpasteurized Wild Forest Honey",
            description = "Directly gathered by indigenous forest foragers from natural rock bee hives in pristine Eastern Ghats flora. 100% NMR tested pure, never micro-filtered or boiled.",
            categoryId = "cat_raw_honey",
            categoryName = "Wild Forest Honey",
            sku = "MN-HNY-WLD-500",
            price = 520.0,
            offerPrice = 475.0,
            availableStock = 110,
            unit = "500 g",
            imageUrl = "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=600&q=80",
            status = ProductStatus.PUBLISHED.name,
            isFeatured = true,
            isNewArrival = false
        ),
        Product(
            id = "prod_a2_ghee_500ml",
            name = "A2 Desi Gir Cow Vedic Bilona Cultured Ghee",
            description = "Hand-churned from curd made of whole A2 milk from free-grazing indigenous Gir cows. Grainy golden texture (Danedar), rich in butyric acid and fat-soluble vitamins.",
            categoryId = "cat_desi_ghee",
            categoryName = "A2 Desi Cow Ghee",
            sku = "MN-GHE-GIR-500",
            price = 980.0,
            offerPrice = 899.0,
            availableStock = 40,
            unit = "500 ml",
            imageUrl = "https://images.unsplash.com/photo-1628088062854-d1870b4553da?w=600&q=80",
            status = ProductStatus.PUBLISHED.name,
            isFeatured = true,
            isNewArrival = false
        ),
        Product(
            id = "prod_lakadong_turmeric_200g",
            name = "Lakadong High Curcumin Organic Turmeric",
            description = "Harvested from the pristine Jaintia Hills of Meghalaya. Tested with 7.5%+ natural curcumin content compared to standard commercial 2%. Potent anti-inflammatory remedy.",
            categoryId = "cat_pure_spices",
            categoryName = "Farm Spices & Turmeric",
            sku = "MN-TUR-LAK-200",
            price = 240.0,
            offerPrice = 210.0,
            availableStock = 120,
            unit = "200 g",
            imageUrl = "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=600&q=80",
            status = ProductStatus.PUBLISHED.name,
            isFeatured = false,
            isNewArrival = true
        ),
        Product(
            id = "prod_ashwagandha_100g",
            name = "Organic Ashwagandha Pure Root Powder",
            description = "Certified organic Withania somnifera root powder. Renowned adaptogen for vitality, stress mitigation, restful sleep, and holistic restorative wellness.",
            categoryId = "cat_ayurvedic_herbs",
            categoryName = "Ayurvedic Powders",
            sku = "MN-HRB-ASH-100",
            price = 220.0,
            offerPrice = 195.0,
            availableStock = 65,
            unit = "100 g",
            imageUrl = "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=600&q=80",
            status = ProductStatus.PUBLISHED.name,
            isFeatured = false,
            isNewArrival = false
        ),
        Product(
            id = "prod_goatmilk_saffron_soap",
            name = "Handmade Goat Milk & Kashmiri Saffron Cold Process Soap",
            description = "Artisanal bath bar created using wood-pressed coconut and castor oils blended with fresh goat milk and pure saffron strands. Gentle, chemical-free pH 7 balanced lather.",
            categoryId = "cat_natural_skincare",
            categoryName = "Natural Skin & Body",
            sku = "MN-SOP-GT-125",
            price = 190.0,
            offerPrice = 170.0,
            availableStock = 90,
            unit = "125 g",
            imageUrl = "https://images.unsplash.com/photo-1608248597359-5452377bcece?w=600&q=80",
            status = ProductStatus.PUBLISHED.name,
            isFeatured = true,
            isNewArrival = true
        )
    )

    val defaultBanners = listOf(
        BannerItem(
            id = "ban_01",
            title = "100% Traditional Wood-Pressed Oils",
            subtitle = "Extracted below 40°C in wooden churners. Pure wellness for your kitchen.",
            imageUrl = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=800&q=80",
            targetCategoryId = "cat_wood_pressed_oils",
            active = true,
            sortOrder = 1
        ),
        BannerItem(
            id = "ban_02",
            title = "Sacred A2 Gir Cow Vedic Bilona Ghee",
            subtitle = "Churned from curd with golden, granular perfection. Direct from ethical pastures.",
            imageUrl = "https://images.unsplash.com/photo-1628088062854-d1870b4553da?w=800&q=80",
            targetCategoryId = "cat_desi_ghee",
            active = true,
            sortOrder = 2
        ),
        BannerItem(
            id = "ban_03",
            title = "Wild Rock Honey & Tribal Harvests",
            subtitle = "Unheated, unfiltered forest goodness tested pure with zero added sugar syrup.",
            imageUrl = "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=800&q=80",
            targetCategoryId = "cat_raw_honey",
            active = true,
            sortOrder = 3
        )
    )
}
