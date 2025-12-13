package ethanjones.cubes.input.movement;

/**
 * State Pattern - Abstract State
 * 
 * Defines the interface for player movement states.
 * Each concrete state determines the movement speed and handles transitions to other states.
 */
public abstract class MovementState {
  
  /**
   * Gets the movement speed for this state
   * @return speed multiplier in blocks per second
   */
  public abstract float getSpeed();
  
  /**
   * Gets the name of this state for debugging/display
   * @return state name
   */
  public abstract String getStateName();
  
  /**
   * Handles state transitions based on input
   * 
   * @param context the movement context
   * @param isSprintPressed whether sprint key (Shift) is held
   * @param isCrouchPressed whether crouch key (Ctrl) is held
   * @param isOnGround whether player is on ground
   * @return the new state (may be this same state or a different one)
   */
  public abstract MovementState handleInput(PlayerMovementContext context, 
                                           boolean isSprintPressed, 
                                           boolean isCrouchPressed,
                                           boolean isOnGround);
  
  /**
   * Called when entering this state
   * @param context the movement context
   */
  public void onEnter(PlayerMovementContext context) {
    // Override if needed
  }
  
  /**
   * Called when exiting this state
   * @param context the movement context
   */
  public void onExit(PlayerMovementContext context) {
    // Override if needed
  }
  
  /**
   * Whether this state allows jumping
   * @return true if jumping is allowed
   */
  public boolean canJump() {
    return true;
  }
  
  /**
   * Whether this state uses vertical movement (flying)
   * @return true if state uses vertical controls
   */
  public boolean hasVerticalMovement() {
    return false;
  }
  
  /**
   * Whether this state can transition to flying state
   * @return true if double-jump to flying is allowed
   */
  public boolean canTransitionToFlying() {
    return true; // Default: most states can transition to flying
  }
  
  /**
   * Whether this state prevents player from falling off block edges
   * (Minecraft-style crouch behavior)
   * @return true if edge protection is active
   */
  public boolean preventsEdgeFall() {
    return false; // Default: no edge protection
  }
}
