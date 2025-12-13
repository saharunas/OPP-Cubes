package ethanjones.cubes.input.movement;

/**
 * State Pattern - Concrete State
 * 
 * Walking state - default movement state
 * Speed: 4.5 blocks/second
 * 
 * Transitions:
 * - Sprint key → SprintingState
 * - Crouch key → CrouchingState
 * - Double jump (handled by context) → FlyingState
 */
public class WalkingState extends MovementState {
  
  private static final float WALKING_SPEED = 4.5f;
  
  @Override
  public float getSpeed() {
    return WALKING_SPEED;
  }
  
  @Override
  public String getStateName() {
    return "Walking";
  }
  
  @Override
  public MovementState handleInput(PlayerMovementContext context, 
                                   boolean isSprintPressed, 
                                   boolean isCrouchPressed,
                                   boolean isOnGround) {
    // Sprint key takes priority
    if (isSprintPressed && isOnGround) {
      return context.getSprintingState();
    }
    
    // Crouch key
    if (isCrouchPressed && isOnGround) {
      return context.getCrouchingState();
    }
    
    // Stay in walking
    return this;
  }
  
  @Override
  public void onEnter(PlayerMovementContext context) {
    // Could add walking sound here
  }
}
