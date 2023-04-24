package gawquon.mapletherm.ui

fun Float.format(place: Int) = "%.${place}f".format(this)
fun Float.format() = "%.${1}f".format(this)
fun String.addUnits(units: String) = "$this $units"