package ethanjones.cubes.input.movement;

/**
 * State Pattern - Concrete State
 * 
 * Flying state - creative mode flight
 * Speed: 10.0 blocks/second (fastest)
 * 
 * Special features:
 * - Uses vertical movement (Space up, Shift down)
 * - Ignores gravity
 * - Toggle mode (double-tap Space to enter/exit)
 * 
 * Transitions:
 * - Double jump → WalkingState (only transition out)
 * - Lands on ground → WalkingState (automatic)
 */
public class FlyingState extends MovementState {
  
  private static final float FLYING_SPEED = 10.0f;
  
  @Override
  public float getSpeed() {
    return FLYING_SPEED;
  }
  
  @Override
  public String getStateName() {
    return "Flying";
  }
  
  @Override
  public MovementState handleInput(PlayerMovementContext context, 
                                   boolean isSprintPressed, 
                                   boolean isCrouchPressed,
                                   boolean isOnGround) {
    // Flying can only transition to Walking
    // (Double-jump toggle is handled in CameraController)
    
    // Auto-exit flying when landing
    if (isOnGround && !context.isFlyingEnabled()) {
      return context.getWalkingState();
    }
    
    return this;
  }
  
  @Override
  public boolean canJump() {
    return false; // Jump key is used for ascending
  }
  
  @Override
  public boolean hasVerticalMovement() {
    return true;
  }
  
  @Override
  public void onEnter(PlayerMovementContext context) {
    // Could add flying particles/effects
  }
  
  @Override
  public void onExit(PlayerMovementContext context) {
    // Could remove flying effects
  }
}
