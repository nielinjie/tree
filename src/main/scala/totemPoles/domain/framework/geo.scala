package totemPoles.domain.framework

object Geo {
  def distance(lat: Double, long: Double, lat2: Double, lon2: Double): Double = {
    val EARTH_RADIUS: Double = 6367450;
    val deltalat = lat2 - lat;
    val deltalon = lon2 - long;
    val a = Math.sin(deltalat / 2) * Math.sin(deltalat / 2) + Math.cos(lat) * Math.cos(lat2) * Math.sin(deltalon / 2) * Math.sin(deltalon / 2);
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    EARTH_RADIUS * c;
  }
}