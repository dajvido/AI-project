package obj


object Time {
  private val form = new java.text.SimpleDateFormat(Values.TIME_FORMAT)
  val startTime = java.util.Calendar.getInstance().getTime.getTime()
  def current = java.util.Calendar.getInstance().getTime.getTime()
  def displayTime = form.format(current - startTime)
}
