# HostPinger

### Approach
I have used the new lifecycle aware `ViewModel` to remove domain logic from the UI. This will help make the testing without involving the UI easier.<br>
`Retrofit` and `RXJava2` make good partners for dealing with the Async tasks<br>
Latency code was adapted from a sample on Stack overflow <br>
Latency results are cached to prevent retesting on rotation.<br>
Latency is only tested when the host is loaded by the `RecyclerView`. This can be seen by scrolling down on a slow connection. It prevent a very long list from being tested all at once, but limiting you to what you can see on the screen.

### Aspects
All objectives have been met. Re-testing of latency is done by clicking the CardViews. 

### Retrospective
Setting up `Dagger2` would have been nice, but I haven't found what "a good one looks like" yet for Kotlin. Plus it's time consuming on a small project.<br>
I decided to make the host list immutable. This is a good thing, but made storing the latency figures (and sorting the Host list) more difficult.<br>
I did have a try at making the recycler re-sort as new latency figures where updated, but it didn't work well, and I think it made the code messy. With more time it would be something I would look to fix.<br>
The image for TradeMe redirecting from http to https was a bit of a headache. Forcing OkHttp3 as the Http client solved this.<br>
More unit test coverage would be good.<br>
The host list could be saved as a property of the Activity. Currently the ViewModel prevents this from being apparent. <br> 
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

