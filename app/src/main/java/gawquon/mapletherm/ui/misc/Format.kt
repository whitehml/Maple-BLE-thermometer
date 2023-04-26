package gawquon.mapletherm.ui.misc

fun Float.format(place: Int) = "%.${place}f".format(this)
fun Float.format() = "%.${1}f".format(this)
fun String.addUnits(units: String) = "$this $units"

// From Chee Yi Ong's Punchthrough tutorial - https://punchthrough.com/android-ble-guide/
fun ByteArray.toHexString(): String =
    joinToString(separator = " ", prefix = "0x") { String.format("%02X", it) }