package obj

class Decision(
			val hours: Int,
			val minutes: Int,
			val x: Int,
			val y: Int,
			val shop1_x: Int,
			val shop1_y: Int,
			val shop2_x: Int,
			val shop2_y: Int,
			val shop3_x: Int,
			val shop3_y: Int,
			val nearestItem_x: Int,
			val nearestItem_y: Int,
			val eq_quantity: Int,
			val estimated_efficiency: Float,
			val unload_need: Boolean = false,
			val isCritHour: Boolean = false,
          ) {}