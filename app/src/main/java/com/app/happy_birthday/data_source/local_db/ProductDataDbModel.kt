package com.app.happy_birthday.data_source.local_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Collections

@Entity
@TypeConverters(Converters::class)
data class ProductDataDbModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "productID") val productID: String,
    @ColumnInfo(name = "entityID") val entityID: String?,
    @ColumnInfo(name = "categoryID") val categoryID: String?,
    @ColumnInfo(name = "productName") val name: String?,
    @ColumnInfo(name = "brandName") val brand: String?,
    @ColumnInfo(name = "sku") val sku: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "quantity") var quantity: Int?,
    @ColumnInfo(name = "finalPrice") val finalPrice: String?,
    @ColumnInfo(name = "regularPrice") val regularPrice: String?,
    @ColumnInfo(name = "discount") val discount: String?,
    @ColumnInfo(name = "remainingQuantity") val remainingQuantity: String?,
    @ColumnInfo(name = "configOptions") val configOptions: List<DBConfigurableOptionModel> = Collections.emptyList()
)


data class DBConfigurableOptionModel(
    val attribute_id: String? = null,
    val type: String? = null,
    val attribute_code: String? = null,
    val attributes: List<DBAttributeOptionModel> = Collections.emptyList(),
    val isSelected: Boolean? = false
)


data class DBAttributeOptionModel(
    val value: String? = null,
    val option_id: String? = null,
    val option_product_id: String? = null
)

