package at.base10.state;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class StateMachineTest {

    public interface Operate {
        int execute(int number);
    }

    public sealed interface AppState extends Operate permits StateSquare, StateInc, StateTransit {
    }

    public static final class SquareFunction {
        int calculate(int number) {
            return number * number;
        }
    }

    public static final class StateSquare extends TransitionalState<AppState> implements AppState {
        private final SquareFunction dummy;

        public StateSquare(SquareFunction dummy, StateMachine<AppState> stateMachine) {
            super(stateMachine);

            this.dummy = dummy;
        }

        @Override
        public int execute(int number) {
            return dummy.calculate(number);
        }

    }

    public static final class StateInc implements AppState {


        @Override
        public int execute(int number) {
            return number + 1;
        }
    }

    public static final class StateTransit extends TransitionalState<AppState> implements AppState {


        public StateTransit(StateMachine<AppState> stateMachine) {
            super(stateMachine);
        }

        @Override
        public int execute(int number) {
            return this.transitionToState(StateInc.class).currentState().execute(number);
        }
    }

    @Test
    public void testInvalidSetupWithNull() {
        //noinspection DataFlowIssue
        assertEquals(
                "initialState is marked non-null but is null",
                assertThrows(NullPointerException.class,
                        () -> StateMachine.builder(AppState.class)
                                .register(sm -> new StateSquare(new SquareFunction(), sm))
                                .register(StateTransit::new)
                                .register(sm -> new StateInc())
                                .build(null)
                ).getMessage()
        );
    }

    @Test
    public void testCreateBuilderWithWithNull() {
        assertEquals(
                "stateClass is marked non-null but is null",
                assertThrows(NullPointerException.class,
                        () -> StateMachine.builder((Class<AppState>) null)
                ).getMessage()
        );
    }

    @Test
    public void testInvalidSetupWithAnonymous() {
        //noinspection Convert2Lambda

        var msg = assertThrows(IllegalArgumentException.class,
                () -> StateMachine.builder(Operate.class)
                        .register(sm -> new Operate() {
                            @Override
                            public int execute(int number) {
                                return 0;
                            }
                        })
                        .build(Operate.class)
        ).getMessage();
        System.out.println(msg);
        assertThat(msg, Matchers.equalTo("Invalid class (Anonymous) class at.base10.state.StateMachineTest$1"));
    }

    @Test
    public void testInvalidSetupWithSynthetic() {
        assertThat(
                assertThrows(IllegalArgumentException.class,
                        () -> StateMachine.builder(Operate.class)
                                .register(sm -> number -> 0)
                                .build(Operate.class)
                ).getMessage(),
                Matchers.startsWith("Invalid class (Synthetic) class at.base10.state.StateMachineTest$$Lambda$")
        );
    }

    @Test
    public void testRegisterNull() {
        //noinspection DataFlowIssue
        assertEquals("stateBuilder is marked non-null but is null",
                assertThrows(
                        NullPointerException.class,
                        () -> StateMachine.builder(AppState.class)
                                .register((StateFactory<AppState>) null)
                ).getMessage()
        );

    }

    @Test
    public void testRegisterNullElement() {
        var list = new ArrayList<StateFactory<AppState>>();
        list.add(null);

        assertEquals("stateBuilder is marked non-null but is null",
                assertThrows(
                        NullPointerException.class,
                        () -> StateMachine.builder(AppState.class).register(list)
                ).getMessage()
        );

    }

    @Test
    public void testRegisterTransit() {
        var stateMachine = StateMachine.builder(AppState.class)
                .register(StateTransit::new)
                .build(StateTransit.class);
        assertInstanceOf(StateTransit.class, stateMachine.currentState());
    }

    @Test
    public void testInvalidInitialState() {
        assertThat(
                assertThrows(IllegalArgumentException.class,
                        () -> StateMachine.builder(AppState.class)
                                .register(sm -> new StateSquare(new SquareFunction(), sm))
                                .register(sm -> new StateInc())
                                .build(StateTransit.class)).getMessage(),
                Matchers.equalTo("State class at.base10.state.StateMachineTest$StateTransit not found")
        );
    }

    @Test
    public void testInvalidTransitionState() {
        assertThat(
                assertThrows(IllegalArgumentException.class,
                        () -> StateMachine.builder(AppState.class)
                                .register(sm -> new StateSquare(new SquareFunction(), sm))
                                .register(sm -> new StateInc())
                                .build(StateInc.class)
                                .transitionToState(StateTransit.class)
                ).getMessage(),
                Matchers.equalTo("State class at.base10.state.StateMachineTest$StateTransit not found")
        );
    }


    @Test
    public void proxy() {
        StateMachine<Operate> stateMachine = StateMachine.builder(Operate.class)
                .register(sm -> new StateInc())
                .build(StateInc.class);
        var proxy = stateMachine.asState();
        assertEquals(3, proxy.execute(2));
        assertInstanceOf(StateInc.class, stateMachine.currentState());
        assertInstanceOf(Operate.class, proxy);
    }


    @Nested
    class BasicTests {
        StateMachine<AppState> stateMachine;

        @BeforeEach
        public void setUp() {
            //noinspection Convert2Lambda,Anonymous2MethodRef
            stateMachine = StateMachine.builder(AppState.class)
                    .register(Set.of(
                            sm -> new StateSquare(new SquareFunction(), sm),
                            new StateFactory<>() {
                                @Override
                                public AppState build(StateMachine<AppState> stateMachine) {
                                    return new StateTransit(stateMachine);
                                }
                            }
                    )).register(
                            sm -> new StateInc()
                    ).build(StateTransit.class);
        }

        @Test
        public void test_initialState() {
            assertInstanceOf(StateTransit.class, stateMachine.currentState());
        }

        @Test
        public void test_transition() {
            assertInstanceOf(StateTransit.class, stateMachine.currentState());
            stateMachine.transitionToState(StateInc.class);
            assertInstanceOf(StateInc.class, stateMachine.currentState());
        }

        @Test
        public void proxy() {
            var proxy = stateMachine.asState(Operate.class);
            assertInstanceOf(StateTransit.class, stateMachine.currentState());
            assertEquals(3, proxy.execute(2));
            assertInstanceOf(StateInc.class, stateMachine.currentState());
            assertInstanceOf(Operate.class, proxy);
        }

        @Test
        public void test_transitional() {
            assertInstanceOf(StateTransit.class, stateMachine.currentState());
            assertEquals(3, stateMachine.currentState().execute(2));
            assertInstanceOf(StateInc.class, stateMachine.currentState());
        }

        @Test
        public void test_transition_to_null() {
            assertInstanceOf(StateTransit.class, stateMachine.currentState());
            assertThrows(NullPointerException.class,
                    () -> stateMachine.transitionToState(null));
        }

    }
}