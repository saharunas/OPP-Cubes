package ethanjones.cubes.input.movement;

/**
 * State Pattern - Context
 * 
 * Manages the current movement state and provides access to all state instances.
 * Maintains single instances of each state (Flyweight pattern).
 */
public class PlayerMovementContext {
  
  // Singleton state instances (Flyweight pattern)
  private final WalkingState walkingState = new WalkingState();
  private final SprintingState sprintingState = new SprintingState();
  private final CrouchingState crouchingState = new CrouchingState();
  private final FlyingState flyingState = new FlyingState();
  
  // Current state
  private MovementState currentState;
  
  // Flying toggle flag (managed by CameraController double-jump logic)
  private boolean flyingEnabled = false;
  
  public PlayerMovementContext() {
    // Start in walking state
    this.currentState = walkingState;
  }
  
  /**
   * Changes the current state
   * Calls onExit on old state and onEnter on new state
   */
  public void setState(MovementState newState) {
    if (currentState != newState) {
      currentState.onExit(this);
      currentState = newState;
      currentState.onEnter(this);
    }
  }
  
  /**
   * Gets the current movement speed
   */
  public float getCurrentSpeed() {
    return currentState.getSpeed();
  }
  
  /**
   * Gets the current state name
   */
  public String getCurrentStateName() {
    return currentState.getStateName();
  }
  
  /**
   * Updates state based on input
   */
  public void updateState(boolean isSprintPressed, boolean isCrouchPressed, boolean isOnGround) {
    MovementState newState = currentState.handleInput(this, isSprintPressed, isCrouchPressed, isOnGround);
    setState(newState);
  }
  
  /**
   * Checks if currently in flying state
   */
  public boolean isFlying() {
    return currentState instanceof FlyingState;
  }
  
  /**
   * Checks if current state allows jumping
   */
  public boolean canJump() {
    return currentState.canJump();
  }
  
  /**
   * Checks if current state has vertical movement
   */
  public boolean hasVerticalMovement() {
    return currentState.hasVerticalMovement();
  }
  
  /**
   * Checks if current state can transition to flying
   */
  public boolean canTransitionToFlying() {
    return currentState.canTransitionToFlying();
  }
  
  /**
   * Checks if current state prevents edge falls (Minecraft crouch)
   */
  public boolean preventsEdgeFall() {
    return currentState.preventsEdgeFall();
  }
  
  // Flying toggle management (for double-jump detection in CameraController)
  
  public boolean isFlyingEnabled() {
    return flyingEnabled;
  }
  
  public void setFlyingEnabled(boolean enabled) {
    this.flyingEnabled = enabled;
    if (enabled) {
      setState(flyingState);
    } else if (isFlying()) {
      setState(walkingState);
    }
  }
  
  public void toggleFlying() {
    setFlyingEnabled(!flyingEnabled);
  }
  
  // State instance accessors (for state transitions)
  
  public WalkingState getWalkingState() {
    return walkingState;
  }
  
  public SprintingState getSprintingState() {
    return sprintingState;
  }
  
  public CrouchingState getCrouchingState() {
    return crouchingState;
  }
  
  public FlyingState getFlyingState() {
    return flyingState;
  }
  
  /**
   * Gets current state (for debugging)
   */
  public MovementState getCurrentState() {
    return currentState;
  }
}
