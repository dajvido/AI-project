package obj

/**
 * Created by dajvido on 04.05.15.
 */
object Time {
  private val form = new java.text.SimpleDateFormat("mm:ss")
  val startTime = java.util.Calendar.getInstance().getTime.getTime()
  def current = java.util.Calendar.getInstance().getTime.getTime()
  def displayTime = form.format(current - startTime)
}
