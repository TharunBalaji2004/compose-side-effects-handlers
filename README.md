![Compose Side Effects Handlers](https://github.com/TharunBalaji2004/compose-side-effects-handlers/assets/95350584/97658c61-d27e-483d-bfed-b392db333570)

# Side Effects Handlers in Compose

Hey there Android devs 👋, This article helps in managing side effects in Jetpack Compose which is essential for building robust and efficient Android applications explained with a list of available side effects handlers and examples.

Hey just a minute 🙋‍♂️ star this GitHub repo for future reference: [TharunBalaji2004/compose-side-effects-handlers](https://github.com/TharunBalaji2004/compose-side-effects-handlers)

## What is meant by side effects?

From the official documentation of Android, A **side-effect** is a change to the state of the app that happens outside the scope of a composable function. In simpler terms, any function, statement, or action that should not be done by any other composable is known as a side effect.

Lets understand it with a simpler example!

```kotlin
var counter = 1

@Composable
fun DisplayText() {
    counter++;
    Text(text = "Hello there!")
}
```

From the above code as we can see that, the Composable `DisplayText()` should display only the text "Hello there!", but the increment statement of `counter` variable is present inside the code, which is out of scope for the respective composable. Some examples of side effects are:

* Performance side effects - Memory Usage, UI Performance
    
* State Management side effects
    
* Lifecycle side effects
    
* Coroutines side effects
    

## What is side effect Handlers?

In Jetpack Compose, side effect handlers are a way to manage and handle side effects, such as asynchronous operations or interactions with the external world (e.g., network requests, database queries, or sensor data), in a declarative and controlled manner.

The list of side effect handlers are:

* [SideEffect](#sideeffect)
    
* [LaunchedEffect](#launchedeffect)
    
* [DisposableEffect](#disposableeffect)
    
* [rememberUpdatedState](#rememberupdatedstate)
    
* [rememberCoroutineScope](#remembercoroutinescope)
    
* [produceState](#producestate)
    
* [derviedStateOf](#derivedstateof)
    

## SideEffect

SideEffect is a side-effect API in Jetpack Compose that allows you to perform asynchronous operations and update the UI in a single lambda function. It takes single argument, `block` to define the statements that has to be executed.

The SideEffect is executed on every recomposition of the parent composable, which means that the operations are executed and updated reactively. It is important to use it carefully, as it can lead to performance problems if it is used incorrectly.

Consider the following code:

```kotlin
@Composable
fun ListOfNames() {
    val nameList = remember { mutableStateOf(listOf<String>()) }
    nameList.value = fetchData()  // Newtowrk Call

    LazyColumn {
        items(nameList.value) { item ->
            Card(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(bottom = 5.dp)) {
                Text(text = item, modifier = Modifier.padding(10.dp))
            }
        }
    }
}
```

There exist two problems with the above code snippet:

* The network call `fetchData()` could be called multiple times by user, which causes server overload by receiving high volume of requests at a time
    
* The network call `fetchData()` might take more time to respond or send data from the server to application
    

To solve these problems, the SideEffect executes asynchronous operations such as API calls, coroutine execution, etc whenever the recomposition of parent composable occurs.

```kotlin
@Composable
fun ListOfNames() {
    val nameList = remember { mutableStateOf(listOf<String>()) }
    
    // Gets executed when Recomposition occurs
    SideEffect {
        nameList.value = fetchData()
    }

    LazyColumn {
        items(nameList.value) { item ->
            Card(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(bottom = 5.dp)) {
                Text(text = item, modifier = Modifier.padding(10.dp))
            }
        }
    }
}
```

But SideEffect is not the feasible method for performing async network calls, since it executes multiple times as recomposition occurs. For these operations, the LaunchedEffect comes into the picture.

## LaunchedEffect

LaunchedEffect is a side-effect API in Jetpack Compose that allows you to perform asynchronous operations and update the UI in a single lambda function. It takes two arguments, `key` for representing when should it be executed and `block` to declare the lambda function.

The suspend function is executed in `CoroutineScope` when the LaunchedEffect enters the composition for the first time, and it is also executed whenever the value of any of the parameters changes.

The LaunchedEffect helps to control asynchronous operations such as API calls, coroutine execution, etc whenever the `key` changes. If the `key` is not defined, then it gets executed only when the initial composition occurs and updates the UI.

```kotlin
@Composable
fun ListOfNames() {
    val nameList = remember { mutableStateOf(listOf<String>()) }
    
    // Gets executed when initial Composition occurs
    LaunchedEffect(key1 = Unit, block = {
        nameList.value = fetchData()
    })

    LazyColumn {
        items(nameList.value) { item ->
            Card(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(bottom = 5.dp)) {
                Text(text = item, modifier = Modifier.padding(10.dp))
            }
        }
    }
}
```

## DisposableEffect
DisposableEffect is a side-effect API in Jetpack Compose, used for managing resources that need to be acquired and released when a Composable is created and destroyed. It's especially useful for managing resources that have a lifecycle associated with a Composable and need to be cleaned up when the Composable is no longer active.

1. **First Composition**: The code inside the `DisposableEffect` block is executed when the Composable is first composed. This is typically when the Composable is initially displayed or when its parent Composable is initially composed.
    
2. **Removal from Composition**: The code inside the `onDispose` block is executed when the Composable is removed from the composition. This occurs when the Composable is no longer needed or when its key changes.

Consider the following code snippet:

```kotlin
@Composable
fun ListOfNames() {
    val nameList = remember { mutableStateOf(listOf<String>()) }
    
    // Gets executed when initial Composition occurs
    DisposableEffect(key1 = Unit, effect = {
        nameList.value = fetchData()
        onDispose {    // Gets executed when composition gets destroyed
            nameList.value = emptyList()
        }
    })


    LazyColumn {
        items(nameList.value) { item ->
            Card(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(bottom = 5.dp)) {
                Text(text = item, modifier = Modifier.padding(10.dp))
            }
        }
    }
}
```

By using DisposableEffect, we can ensure that resources are properly managed and cleaned up when they are no longer needed, which is crucial for the overall stability and performance of your Composable-based UI.

## rememberCoroutineScope

The rememberCoroutineScope is a utility function provided by Jetpack Compose to create a coroutine scope that is bound to the Composable function's lifecycle. This is particularly useful when you need to launch and manage coroutines within Jetpack Compose components, ensuring that they are canceled appropriately when the Composable is removed from the UI hierarchy.

```kotlin
@Composable
fun HasSideEffect() {
    val counter = remember { mutableStateOf(0) }
    var coroutineScope = rememberCoroutineScope()
    
    var text = "Counter is running ${counter.value}"
    if (counter.value == 10) text = "Counter stopped"
    
    Column {
        Text(text = text)
        Button(onClick = {
            coroutineScope.launch {
                Log.d("DEBUG", "Started....")
            
                try {
                    for (i in 1..10){
                        counter.value++
                        delay(1000)
                    } 
                } catch(e: Exception) {
                    Log.d("DEBUG",e.message.toString())
                }
            }
        }) {
            Text(text = "START")
        }
    }
}
```

From the above code snippet, as we can see the `counter` value changes after each second. The only difference between LaunchedEffect and rememberCoroutineScope is, the LaunchedEffect is a Composable so it requires another parent Composable to declare whereas remeberCoroutineScope is a utility function which means it can be declared during any event or within any Composable.

## rememberUpdatedState

The rememberUpdatedState is a side effect handler provided by Jetpack Compose that is used to ensure that a Composable always reflects the most up-to-date state when it's recomposed. It's particularly useful when you need to work with a snapshot of some state that might change during the composition process.

Consider the code snippet:

```kotlin
@Composable
fun App() {
    var counter = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = Unit, block = {
        delay(1000)
        counter.value = 10
    })

    Counter(counter.value)
}

@Composable
fun Counter(value: Int) {
    val state = rememberUpdatedState(newValue = value)
   
    LaunchedEffect(key1 = Unit, block = {
        delay(3000)
        Log.d("counter",state.value.toString())
    })

    Text(text = value.toString())
}
```

Here, the `App` Composable passes the updated value of `counter` to `Counter` Composable, to maintain the recent or updated value, `rememberUpdatedState` holds the new value and Log statement prints the value held by `state`

This is particularly useful when you want to capture the initial state for some calculation or display purposes and ensure that it doesn't change during the composition process, preventing unexpected behavior or bugs.

## produceState

The produceState is a function provided by Jetpack Compose that allows you to create a custom `State` object in a Composable that can be read and updated within a Composable's recomposition cycle. It's similar to `mutableStateOf`, but it also provides a way to produce new state values based on the current state and an update function.

This can be useful when you want to derive new state from existing state or perform some side effects when the state changes. The produceState accepts three parameters:

* `initialValue`: The initial value of the state.
    
* `producer`: A function that allows you to update the state.
    

```kotlin
@Composable
fun ProducedState() {
    // prodcues custom state using custom producer function
    val state = produceState(initialValue = 0, producer = {
        for (i in 1..10){
            delay(1000)
            value++
        }
    })

    Text(
        text = state.value.toString(), 
        style = MaterialTheme.typography.headlineLarge
    )
}
```

## derviedStateOf

The derivedStateOf function provided by Jetpack Compose allows you to compute a derived or derived read-only state based on one or more other states or values. It ensures that the derived state is recomposed only when one of its dependencies changes.

Inside the lambda passed to `derivedStateOf`, you can calculate the derived state using the values of other states or variables. Jetpack Compose automatically tracks the dependencies of the derived state and recomposes the Composable only when one of those dependencies changes.

```kotlin
@Composable
fun DerivedState() {
    var count = remember { mutableStateOf(0) }
    var doubleCount = remember { mutableStateOf(0) }

    // Calculate a derived state: double the count
    val derivedDoubleCount = derivedStateOf {
        doubleCount = count * 2
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Count: $count", fontSize = 24.sp)
        Text(text = "Double Count: $derivedDoubleCount", fontSize = 24.sp)

        Button(
            onClick = {
                // Update the count
                count++
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Increment Count")
        }
    }
}
```

That's all about Side Effect Handlers, try developing apps with these handlers implementing network and async calls. See you in the next article! 😊✅
