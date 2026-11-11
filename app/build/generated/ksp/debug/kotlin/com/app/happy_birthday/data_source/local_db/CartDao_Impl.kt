package com.app.happy_birthday.data_source.local_db

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performBlocking
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CartDao_Impl(
  __db: RoomDatabase,
) : CartDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfProductDataDbModel: EntityInsertAdapter<ProductDataDbModel>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfProductDataDbModel = object : EntityInsertAdapter<ProductDataDbModel>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `ProductDataDbModel` (`id`,`productID`,`entityID`,`categoryID`,`productName`,`brandName`,`sku`,`image`,`quantity`,`finalPrice`,`regularPrice`,`discount`,`remainingQuantity`,`configOptions`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ProductDataDbModel) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.productID)
        val _tmpEntityID: String? = entity.entityID
        if (_tmpEntityID == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpEntityID)
        }
        val _tmpCategoryID: String? = entity.categoryID
        if (_tmpCategoryID == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpCategoryID)
        }
        val _tmpName: String? = entity.name
        if (_tmpName == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpName)
        }
        val _tmpBrand: String? = entity.brand
        if (_tmpBrand == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpBrand)
        }
        val _tmpSku: String? = entity.sku
        if (_tmpSku == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpSku)
        }
        val _tmpImage: String? = entity.image
        if (_tmpImage == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpImage)
        }
        val _tmpQuantity: Int? = entity.quantity
        if (_tmpQuantity == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpQuantity.toLong())
        }
        val _tmpFinalPrice: String? = entity.finalPrice
        if (_tmpFinalPrice == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpFinalPrice)
        }
        val _tmpRegularPrice: String? = entity.regularPrice
        if (_tmpRegularPrice == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpRegularPrice)
        }
        val _tmpDiscount: String? = entity.discount
        if (_tmpDiscount == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmpDiscount)
        }
        val _tmpRemainingQuantity: String? = entity.remainingQuantity
        if (_tmpRemainingQuantity == null) {
          statement.bindNull(13)
        } else {
          statement.bindText(13, _tmpRemainingQuantity)
        }
        val _tmp: String = __converters.fromConfigList(entity.configOptions)
        statement.bindText(14, _tmp)
      }
    }
  }

  public override fun addProductToCart(vararg products: ProductDataDbModel): Unit =
      performBlocking(__db, false, true) { _connection ->
    __insertAdapterOfProductDataDbModel.insert(_connection, products)
  }

  public override fun getQtyInCart(productEntityID: String?): Int {
    val _sql: String = "SELECT DISTINCT quantity FROM ProductDataDbModel WHERE entityID IN (?)"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        if (productEntityID == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, productEntityID)
        }
        val _result: Int
        if (_stmt.step()) {
          _result = _stmt.getLong(0).toInt()
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTotalCartProductCount(): List<ProductDataDbModel> {
    val _sql: String = "SELECT * FROM ProductDataDbModel"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfProductID: Int = getColumnIndexOrThrow(_stmt, "productID")
        val _columnIndexOfEntityID: Int = getColumnIndexOrThrow(_stmt, "entityID")
        val _columnIndexOfCategoryID: Int = getColumnIndexOrThrow(_stmt, "categoryID")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "productName")
        val _columnIndexOfBrand: Int = getColumnIndexOrThrow(_stmt, "brandName")
        val _columnIndexOfSku: Int = getColumnIndexOrThrow(_stmt, "sku")
        val _columnIndexOfImage: Int = getColumnIndexOrThrow(_stmt, "image")
        val _columnIndexOfQuantity: Int = getColumnIndexOrThrow(_stmt, "quantity")
        val _columnIndexOfFinalPrice: Int = getColumnIndexOrThrow(_stmt, "finalPrice")
        val _columnIndexOfRegularPrice: Int = getColumnIndexOrThrow(_stmt, "regularPrice")
        val _columnIndexOfDiscount: Int = getColumnIndexOrThrow(_stmt, "discount")
        val _columnIndexOfRemainingQuantity: Int = getColumnIndexOrThrow(_stmt, "remainingQuantity")
        val _columnIndexOfConfigOptions: Int = getColumnIndexOrThrow(_stmt, "configOptions")
        val _result: MutableList<ProductDataDbModel> = mutableListOf()
        while (_stmt.step()) {
          val _item: ProductDataDbModel
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpProductID: String
          _tmpProductID = _stmt.getText(_columnIndexOfProductID)
          val _tmpEntityID: String?
          if (_stmt.isNull(_columnIndexOfEntityID)) {
            _tmpEntityID = null
          } else {
            _tmpEntityID = _stmt.getText(_columnIndexOfEntityID)
          }
          val _tmpCategoryID: String?
          if (_stmt.isNull(_columnIndexOfCategoryID)) {
            _tmpCategoryID = null
          } else {
            _tmpCategoryID = _stmt.getText(_columnIndexOfCategoryID)
          }
          val _tmpName: String?
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName)
          }
          val _tmpBrand: String?
          if (_stmt.isNull(_columnIndexOfBrand)) {
            _tmpBrand = null
          } else {
            _tmpBrand = _stmt.getText(_columnIndexOfBrand)
          }
          val _tmpSku: String?
          if (_stmt.isNull(_columnIndexOfSku)) {
            _tmpSku = null
          } else {
            _tmpSku = _stmt.getText(_columnIndexOfSku)
          }
          val _tmpImage: String?
          if (_stmt.isNull(_columnIndexOfImage)) {
            _tmpImage = null
          } else {
            _tmpImage = _stmt.getText(_columnIndexOfImage)
          }
          val _tmpQuantity: Int?
          if (_stmt.isNull(_columnIndexOfQuantity)) {
            _tmpQuantity = null
          } else {
            _tmpQuantity = _stmt.getLong(_columnIndexOfQuantity).toInt()
          }
          val _tmpFinalPrice: String?
          if (_stmt.isNull(_columnIndexOfFinalPrice)) {
            _tmpFinalPrice = null
          } else {
            _tmpFinalPrice = _stmt.getText(_columnIndexOfFinalPrice)
          }
          val _tmpRegularPrice: String?
          if (_stmt.isNull(_columnIndexOfRegularPrice)) {
            _tmpRegularPrice = null
          } else {
            _tmpRegularPrice = _stmt.getText(_columnIndexOfRegularPrice)
          }
          val _tmpDiscount: String?
          if (_stmt.isNull(_columnIndexOfDiscount)) {
            _tmpDiscount = null
          } else {
            _tmpDiscount = _stmt.getText(_columnIndexOfDiscount)
          }
          val _tmpRemainingQuantity: String?
          if (_stmt.isNull(_columnIndexOfRemainingQuantity)) {
            _tmpRemainingQuantity = null
          } else {
            _tmpRemainingQuantity = _stmt.getText(_columnIndexOfRemainingQuantity)
          }
          val _tmpConfigOptions: List<DBConfigurableOptionModel>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfConfigOptions)
          _tmpConfigOptions = __converters.toConfigList(_tmp)
          _item =
              ProductDataDbModel(_tmpId,_tmpProductID,_tmpEntityID,_tmpCategoryID,_tmpName,_tmpBrand,_tmpSku,_tmpImage,_tmpQuantity,_tmpFinalPrice,_tmpRegularPrice,_tmpDiscount,_tmpRemainingQuantity,_tmpConfigOptions)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun isProductPresentInCart(productEntityID: String?): Boolean {
    val _sql: String = "SELECT entityID FROM ProductDataDbModel WHERE entityID=?"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        if (productEntityID == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, productEntityID)
        }
        val _result: Boolean
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp != 0
        } else {
          _result = false
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllCartProducts(): List<ProductDataDbModel> {
    val _sql: String = "SELECT * FROM ProductDataDbModel"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfProductID: Int = getColumnIndexOrThrow(_stmt, "productID")
        val _columnIndexOfEntityID: Int = getColumnIndexOrThrow(_stmt, "entityID")
        val _columnIndexOfCategoryID: Int = getColumnIndexOrThrow(_stmt, "categoryID")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "productName")
        val _columnIndexOfBrand: Int = getColumnIndexOrThrow(_stmt, "brandName")
        val _columnIndexOfSku: Int = getColumnIndexOrThrow(_stmt, "sku")
        val _columnIndexOfImage: Int = getColumnIndexOrThrow(_stmt, "image")
        val _columnIndexOfQuantity: Int = getColumnIndexOrThrow(_stmt, "quantity")
        val _columnIndexOfFinalPrice: Int = getColumnIndexOrThrow(_stmt, "finalPrice")
        val _columnIndexOfRegularPrice: Int = getColumnIndexOrThrow(_stmt, "regularPrice")
        val _columnIndexOfDiscount: Int = getColumnIndexOrThrow(_stmt, "discount")
        val _columnIndexOfRemainingQuantity: Int = getColumnIndexOrThrow(_stmt, "remainingQuantity")
        val _columnIndexOfConfigOptions: Int = getColumnIndexOrThrow(_stmt, "configOptions")
        val _result: MutableList<ProductDataDbModel> = mutableListOf()
        while (_stmt.step()) {
          val _item: ProductDataDbModel
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpProductID: String
          _tmpProductID = _stmt.getText(_columnIndexOfProductID)
          val _tmpEntityID: String?
          if (_stmt.isNull(_columnIndexOfEntityID)) {
            _tmpEntityID = null
          } else {
            _tmpEntityID = _stmt.getText(_columnIndexOfEntityID)
          }
          val _tmpCategoryID: String?
          if (_stmt.isNull(_columnIndexOfCategoryID)) {
            _tmpCategoryID = null
          } else {
            _tmpCategoryID = _stmt.getText(_columnIndexOfCategoryID)
          }
          val _tmpName: String?
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName)
          }
          val _tmpBrand: String?
          if (_stmt.isNull(_columnIndexOfBrand)) {
            _tmpBrand = null
          } else {
            _tmpBrand = _stmt.getText(_columnIndexOfBrand)
          }
          val _tmpSku: String?
          if (_stmt.isNull(_columnIndexOfSku)) {
            _tmpSku = null
          } else {
            _tmpSku = _stmt.getText(_columnIndexOfSku)
          }
          val _tmpImage: String?
          if (_stmt.isNull(_columnIndexOfImage)) {
            _tmpImage = null
          } else {
            _tmpImage = _stmt.getText(_columnIndexOfImage)
          }
          val _tmpQuantity: Int?
          if (_stmt.isNull(_columnIndexOfQuantity)) {
            _tmpQuantity = null
          } else {
            _tmpQuantity = _stmt.getLong(_columnIndexOfQuantity).toInt()
          }
          val _tmpFinalPrice: String?
          if (_stmt.isNull(_columnIndexOfFinalPrice)) {
            _tmpFinalPrice = null
          } else {
            _tmpFinalPrice = _stmt.getText(_columnIndexOfFinalPrice)
          }
          val _tmpRegularPrice: String?
          if (_stmt.isNull(_columnIndexOfRegularPrice)) {
            _tmpRegularPrice = null
          } else {
            _tmpRegularPrice = _stmt.getText(_columnIndexOfRegularPrice)
          }
          val _tmpDiscount: String?
          if (_stmt.isNull(_columnIndexOfDiscount)) {
            _tmpDiscount = null
          } else {
            _tmpDiscount = _stmt.getText(_columnIndexOfDiscount)
          }
          val _tmpRemainingQuantity: String?
          if (_stmt.isNull(_columnIndexOfRemainingQuantity)) {
            _tmpRemainingQuantity = null
          } else {
            _tmpRemainingQuantity = _stmt.getText(_columnIndexOfRemainingQuantity)
          }
          val _tmpConfigOptions: List<DBConfigurableOptionModel>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfConfigOptions)
          _tmpConfigOptions = __converters.toConfigList(_tmp)
          _item =
              ProductDataDbModel(_tmpId,_tmpProductID,_tmpEntityID,_tmpCategoryID,_tmpName,_tmpBrand,_tmpSku,_tmpImage,_tmpQuantity,_tmpFinalPrice,_tmpRegularPrice,_tmpDiscount,_tmpRemainingQuantity,_tmpConfigOptions)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun updateProductsInCart(strQty: Int?, productEntityID: String?) {
    val _sql: String = "UPDATE ProductDataDbModel SET quantity =? WHERE entityID = ?"
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        if (strQty == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindLong(_argIndex, strQty.toLong())
        }
        _argIndex = 2
        if (productEntityID == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, productEntityID)
        }
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun updateProductsInCartOffline(
    strQty: String?,
    productID: String?,
    productEntityID: String?,
    configurableOptionModelJson: String?,
  ) {
    val _sql: String =
        "UPDATE ProductDataDbModel SET quantity =?,productID =?,entityID =?,configOptions = ? WHERE entityID = ?"
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        if (strQty == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, strQty)
        }
        _argIndex = 2
        if (productID == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, productID)
        }
        _argIndex = 3
        if (productEntityID == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, productEntityID)
        }
        _argIndex = 4
        if (configurableOptionModelJson == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, configurableOptionModelJson)
        }
        _argIndex = 5
        if (productEntityID == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, productEntityID)
        }
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun deleteProductFromCart(productEntityID: String?) {
    val _sql: String = "DELETE from ProductDataDbModel where entityID=?"
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        if (productEntityID == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, productEntityID)
        }
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun deleteCartTable() {
    val _sql: String = "DELETE FROM ProductDataDbModel"
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
