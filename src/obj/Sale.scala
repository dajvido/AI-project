package obj

class Sale(
            val shop: Int,
            val item: Int,
            val eq_quantity: Int,
            val negotiationSuccess: Boolean = true,
            val isProper: Boolean = true,
            val isCritHour: Boolean = false
            ) {}