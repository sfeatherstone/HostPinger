# HostPinger

### Approach
I have used the new lifecycle aware `ViewModel` to remove domain logic from the UI. This will help make the testing without involving the UI easier.<br>
`Retrofit` and `RXJava2` make good partners for dealing with the Async tasks<br>
Latency code was adapted from a sample on Stack overflow <br>


### Third Party Libraries
Google support libs including `Constraint-layout` `Cardview`.<br>
`Android lifecycle architecture components` for the lifecycle aware ViewModel and LiveData<br>
`Retrofit2` for Rest/Http calls. Overkill for a single http call, but how I would want to build something bigger out <br>
`RxJava2` for async handling.
`Picasso` for image fetching/displaying. <br>
`picasso2-okhttp3-downloader` to deal with redirect problem caused by TradeMe image redirecting from http to https

