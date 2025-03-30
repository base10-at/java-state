# State Machine Library (at.base10.state)

## Overview
The `at.base10.state` package provides a flexible and extensible state machine framework for Java applications. It allows developers to define states, manage transitions, and build state machines with ease.

## Features
- **State Machine Management**: Define and transition between states dynamically.
- **Factory-Based State Creation**: Use `StateFactory` to create states.
- **Builder Pattern**: Construct state machines using `StateMachineBuilder`.
- **Proxy-Based State Representation**: Retrieve states as proxy instances.

## Installation
To use this library, add the following dependency to your Maven `pom.xml`:

```xml
<dependency>
    <groupId>at.base10</groupId>
    <artifactId>state</artifactId>
    <version>latest</version>
</dependency>
```

For Gradle:

```gradle
dependencies {
    implementation 'at.base10:state:latest'
}
```

## Usage
### Creating a State Machine
Define states and build a state machine using `StateMachineBuilder`:

```java
import at.base10.state.*;

interface AppState {
        void execute();
        // more methods to handle operations
}
class ConcreteState implements AppState{
    //... implement state logic here
}
class AnotherState implements AppState{
    //... implement state logic here
}

public class Example {
    public static void main(String[] args) {
        StateMachine<AppState> stateMachine = StateMachine.builder(AppState.class)
                .register(stateMachine -> new ConcreteState())
                .register(stateMachine -> new AnotherState())
                .build(ConcreteState.class);
        
        System.out.println("Current State: " + stateMachine.currentState());
    }
}
```

### Transitioning Between States
```java
public void doStuff(StateMachine<AppState> stateMachine){
    // ... 
    stateMachine.transitionToState(AnotherState.class);
}
```


### Make States aware of the Context

```java

import at.base10.state.StateMachine;
import at.base10.state.TransitionalState;

interface AppState {
    void execute();
}

class ConcreteState extends TransitionalState<AppState> implements AppState {
    ConcreteState(StateMachine<AppState> stateMachine) {
        super(stateMachine);
    }

    public void execute() {
        // ... 
        this.transitionToState(AnotherState.class);
    }
}
```


### create a proxy for the current state

```java
import at.base10.state.ContextAwareState;
import at.base10.state.StateMachine;

interface AppState {
    void execute();
}

/**
 * this is not an actual state of the StateMachine, but rather a proxy acting as the current state
 */
class ProxyState extends ContextAwareState<AppState> implements AppState {
    ProxyState(StateMachine<AppState> stateMachine) {
        super(stateMachine);
    }
    
    public void execute() {
        this.stateMachine.currentState().execute();
    }
}

public AppState getProxy(StateMachine<AppState> stateMachine){
    return new ProxyState(stateMachine);
}
```

### Create generic a proxy for the current state
This uses reflection and is slightly less performant, but very convenient, 
since you do not have to create our proxy class or care about initialization.

```java

import at.base10.state.StateMachine;
interface AppState {
    void execute();
}
public AppState getGenericProxy(StateMachine<AppState> stateMachine){
    // ... 
    return stateMachine.asState(AppState.class);
}
```




## Classes and Interfaces
- **`StateFactory<S>`**: Factory interface for creating state instances.
- **`StateMachine<S>`**: Manages states and transitions.
- **`StateMachineBuilder<S>`**: Builds a state machine.
- **`Transitional<S>`**: Base class for states that support transitions.
- **`ContextAwareState<S>`**: Base class for states that are aware of their associated state machine.

## Documentation
For more details, visit the official Javadoc:
[View Javadoc](https://javadoc.io/doc/at.base10/state/latest/at/base10/state/package-summary.html)

## License
This project is licensed under the MIT License.

