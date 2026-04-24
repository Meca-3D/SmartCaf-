package com.ynov.smartcafemobile.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    val price: Double = 0.0,
    val stock: Int = 0,
    val imageUrl: String? = null,
    val isActive: Boolean? = true,
    val category: String = "",
    val categoryObject: Category? = null
) {
    /** Retourne le nom de catégorie depuis l'objet lié, ou le champ texte en fallback */
    val categoryName: String
        get() = categoryObject?.name?.ifBlank { category } ?: category
}

val mockProducts = listOf(
    Product(1, "Sandwich Poulet", "Poulet grillé, salade, tomates, sauce mayo", 5.50, 10, category = "Sandwichs"),
    Product(2, "Coca-Cola 33cl", "Boisson rafraîchissante, servi bien frais", 2.00, 50, category = "Boissons"),
    Product(3, "Salade César", "Salade César avec poulet grillé et croûtons", 7.50, 8, category = "Salades"),
    Product(4, "Brownie Chocolat", "Brownie maison au chocolat noir fondant", 3.50, 15, category = "Desserts"),
    Product(5, "Wrap Végétarien", "Wrap avec légumes grillés et houmous", 6.00, 12, category = "Sandwichs"),
    Product(6, "Jus d'Orange", "Jus d'orange fraîchement pressé 25cl", 3.00, 20, category = "Boissons"),
    Product(7, "Panini Jambon Fromage", "Panini jambon, fromage, tomates, grillé", 4.50, 15, category = "Sandwichs"),
    Product(8, "Café Latte", "Café latte onctueux avec mousse de lait", 3.50, 100, category = "Boissons"),
    Product(9, "Tarte aux Pommes", "Tarte maison aux pommes caramélisées", 4.00, 8, category = "Desserts"),
    Product(10, "Salade Niçoise", "Thon, oeufs, olives, tomates cerises", 8.50, 6, category = "Salades"),
    Product(11, "Chips Barbecue", "Chips croustillantes saveur barbecue", 1.50, 30, category = "Snacks"),
    Product(12, "Smoothie Fruits Rouges", "Fraises, framboises, myrtilles mixés", 4.50, 15, category = "Boissons"),
)

val mockCategories = listOf(
    Category(1, "Sandwichs", "Nos sandwichs maison"),
    Category(2, "Boissons", "Boissons chaudes et froides"),
    Category(3, "Salades", "Salades fraîches"),
    Category(4, "Desserts", "Pâtisseries et desserts"),
    Category(5, "Snacks", "Petites collations"),
)
