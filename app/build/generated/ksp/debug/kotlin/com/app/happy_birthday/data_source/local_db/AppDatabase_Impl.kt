package com.app.happy_birthday.data_source.local_db

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _cartDao: Lazy<CartDao> = lazy {
    CartDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "5b834d3e8537f3abf67efe57f032e105", "55b2bc348736d5efea29d06c80c95ce3") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `ProductDataDbModel` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `productID` TEXT NOT NULL, `entityID` TEXT, `categoryID` TEXT, `productName` TEXT, `brandName` TEXT, `sku` TEXT, `image` TEXT, `quantity` INTEGER, `finalPrice` TEXT, `regularPrice` TEXT, `discount` TEXT, `remainingQuantity` TEXT, `configOptions` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5b834d3e8537f3abf67efe57f032e105')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `ProductDataDbModel`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsProductDataDbModel: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsProductDataDbModel.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("productID", TableInfo.Column("productID", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("entityID", TableInfo.Column("entityID", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("categoryID", TableInfo.Column("categoryID", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("productName", TableInfo.Column("productName", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("brandName", TableInfo.Column("brandName", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("sku", TableInfo.Column("sku", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("image", TableInfo.Column("image", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("quantity", TableInfo.Column("quantity", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("finalPrice", TableInfo.Column("finalPrice", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("regularPrice", TableInfo.Column("regularPrice", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("discount", TableInfo.Column("discount", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("remainingQuantity", TableInfo.Column("remainingQuantity",
            "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProductDataDbModel.put("configOptions", TableInfo.Column("configOptions", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysProductDataDbModel: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesProductDataDbModel: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoProductDataDbModel: TableInfo = TableInfo("ProductDataDbModel",
            _columnsProductDataDbModel, _foreignKeysProductDataDbModel, _indicesProductDataDbModel)
        val _existingProductDataDbModel: TableInfo = read(connection, "ProductDataDbModel")
        if (!_infoProductDataDbModel.equals(_existingProductDataDbModel)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |ProductDataDbModel(com.app.happy_birthday.data_source.local_db.ProductDataDbModel).
              | Expected:
              |""".trimMargin() + _infoProductDataDbModel + """
              |
              | Found:
              |""".trimMargin() + _existingProductDataDbModel)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "ProductDataDbModel")
  }

  public override fun clearAllTables() {
    super.performClear(false, "ProductDataDbModel")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(CartDao::class, CartDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun cartDao(): CartDao? = _cartDao.value
}
