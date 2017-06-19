import java.util.*

/**
 * Created by vk on 27.09.16.
 */
public class CardsStorage {
    public val accounts = ArrayList<AccountData>()
    init {
        initializeStorage();
    }
    fun resetStorage() {
        accounts.clear();
        initializeStorage()
    }
    private fun initializeStorage() {
        accounts.add(AccountData(pan="5449***1331",account_id = 12345678,title_small = "MasterCard1331",title = "Master1",balance = 1000.00f,currency = "RUR"))
        accounts.add(AccountData(pan="5449***1332",account_id = 12345679,title_small = "MasterCard1332",title = "Master2",balance = 100.00f,currency = "RUR"))
        accounts.add(AccountData(pan="5449***1333",account_id = 12345680,title_small = "MasterCard1333",title = "Master3",balance = 10000.00f,currency = "RUR"))
        accounts.add(AccountData(pan="5449***1334",account_id = 12345681,title_small = "MasterCard1334",title = "Master4",balance = 2000.00f,currency = "RUR"))
        accounts.add(AccountData(pan="5449***1335",account_id = 12345682,title_small = "MasterCard1335",title = "Master5",balance = 4000.00f,currency = "RUR"))
        accounts.add(AccountData(pan="5449***1336",account_id = 12345683,title_small = "MasterCard1336",title = "Master6",balance = 6000.00f,currency = "RUR"))

    }
    fun deleteAccount(accid:Int):Boolean {
        return accounts.removeIf {it.account_id==accid}
    }
}

