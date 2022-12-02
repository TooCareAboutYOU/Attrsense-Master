#ViewModel初始化及其异常#
方式一
private val splash2ViewModel:Splash2ViewModel by viewModels()
注：此方法不适用于带参构造，新版会提示异常：Caused by: java.lang.IllegalArgumentException: CreationExtras must have a value by `SAVED_STATE_REGISTRY_OWNER_KEY`

方式二
private val splash2ViewModel:Splash2ViewModel by viewModels{ViewModelFactory()}
inner class ViewModelFactory:ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when(modelClass){
            Splash2ViewModel::class.java -> {
                //通过extras传递自定义参数
                Splash2ViewModel()
            }
            else-> throw IllegalArgumentException("Unknown class $modelClass")
        }as T
    }
}

方式三
private val splash2ViewModel:Splash2ViewModel =ViewModelProvider(this)[Splash2ViewModel::class.java]

方式四-kotlin
private val mainViewModel by lazy {
    viewModelFactory { initializer { MainViewModel(apiService) } }.create(
        MainViewModel::class.java,
        defaultViewModelCreationExtras
    )
}
