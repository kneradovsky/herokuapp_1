/**
 * Created by vk on 27.09.16.
 */
data class AccountData(var pan:String,var account_id:Int,var title_small:String,var title:String,var balance:Float,var currency: String) {
    var design_url:String="/content/images/MasterCard_Gold_EMV_PayPass_ЕжикСобака.png"
    var transactions_total_amount:Int=0
    var IsSalary:Boolean = false
    var tariff_avg_month_balance:Int = 472465
    var type:Int = 1
    var closing_date:String= "2019-10-31T00:00:00"
    var is_blocked:Int=0
    var partial_withdraw_available:Int= 1
    var refill_available:Int=1
    val is_apple_pay_available:Int=1
    var blocked_amount:Float = (-1)*balance
    var scheme_id:String="xint"
}