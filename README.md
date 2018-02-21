# HostPinger - Exploring MVVM vs MVP using Google's Lifetime Aware components
 
## Introduction
I was looking to explore best how to implement a fairly simple task of showing a list of Websites, and getting the latency of connecting to them. 
This lead me to explore how the MVVM and MVP frameworks might look when using Google's new [Lifecycle Aware Components](https://developer.android.com/topic/libraries/architecture/lifecycle.html).<br>

#### MVVM
MVVM sits well the the `ViewModel` class provided and is Lifecycle aware. The ViewModel is not recreated on rotation. LiveData is used to decouple the ViewModel and the View (Activity).   
  
#### MVP
MVP normally has a `interface` or contract between the `View` and the `Presenter`. With lambdas in Kotlin, you can inject code into the `Presenter` and so in my case I did not require  
MVP requires more work (and is still incomplete). It is not yet `Lifecycle` aware.

### Approach
I have used the new lifecycle aware `ViewModel` to remove domain logic from the UI. This will help make the testing without involving the UI easier.<br>
`Retrofit` and `RXJava2` make good partners for dealing with the Async tasks<br>
Latency code was adapted from a [sample](https://stackoverflow.com/a/37868059/113012) on Stack overflow <br>
Latency results are cached to prevent re-testing on rotation.<br>
Latency is only tested when the host is loaded by the `RecyclerView`. 
This can be seen by scrolling down on a slow connection. It prevent a very long list from being tested all at once, but limiting you to what you can see on the screen. (This may change when I change how the threading of the tests are run)
Kotlin experimental coroutines are used for making the latency test non blocking. 

## TODO
Setting up `Dagger2` would have been nice, but I haven't found what "a good one looks like" yet for Kotlin. <br>
The MVP implementation is work in progress. It is not currently lifecycle aware<br>
Get the recycler to resort dynamically when is latency sort order.<br>

### Retrospective
I decided to make the host list immutable. This is a good thing, but made storing the latency figures (and sorting the Host list) more difficult.<br>
The image for TradeMe redirecting from http to https was a bit of a headache. Forcing OkHttp3 as the Http client solved this.<br>
More unit test coverage would be good.<br>
Using the Kotlin viewbinding for ViewHolders was fiddly. And required turning `experimental` on.
The error handling could be improved. Better feedback.<br>
The UI could use the help of a UX designer...<br>

### Third Party Libraries
Google support libs including `Constraint-layout` `Cardview`.<br>
`Android lifecycle architecture components` for the lifecycle aware ViewModel and LiveData<br>
`Retrofit2` for Rest/Http calls. Overkill for a single http call, but how I would want to build something bigger out <br>
`RxJava2` for async handling.
`Picasso` for image fetching/displaying. <br>
`picasso2-okhttp3-downloader` to deal with redirect problem caused by TradeMe image redirecting from http to https

