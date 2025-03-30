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

interface AppState {}
class ConcreteState implements AppState{}
class AnotherState implements AppState{}

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
stateMachine.transitionToState(AnotherState.class);
```

## Classes and Interfaces
- **`StateFactory<S>`**: Factory interface for creating state instances.
- **`StateMachine<S>`**: Manages states and transitions.
- **`StateMachineBuilder<S>`**: Builds a state machine.
- **`Transitional<S>`**: Base class for states that support transitions.

## Documentation
For more details, visit the official Javadoc:
[View Javadoc](https://javadoc.io/doc/at.base10/state/latest/at/base10/state/package-summary.html)

## License
This project is licensed under the MIT License.

