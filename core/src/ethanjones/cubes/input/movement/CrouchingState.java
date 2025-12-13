package ethanjones.cubes.input.movement;

/**
 * State Pattern - Concrete State
 * 
 * Crouching state - slow, careful movement
 * Speed: 2.0 blocks/second (slowest)
 * 
 * Transitions:
 * - Crouch key released → WalkingState
 * - Double jump → FlyingState
 * - In air → WalkingState
 */
public class CrouchingState extends MovementState {
  
  private static final float CROUCHING_SPEED = 2.0f;
  
  @Override
  public float getSpeed() {
    return CROUCHING_SPEED;
  }
  
  @Override
  public String getStateName() {
    return "Crouching";
  }
  
  @Override
  public MovementState handleInput(PlayerMovementContext context, 
                                   boolean isSprintPressed, 
                                   boolean isCrouchPressed,
                                   boolean isOnGround) {
    // Crouch only works on ground - jumping interrupts crouch
    if (!isOnGround) {
      return context.getWalkingState();
    }
    
    // Crouch key released → back to walking
    if (!isCrouchPressed) {
      return context.getWalkingState();
    }
    
    // Continue crouching
    return this;
  }
  
  @Override
  public boolean preventsEdgeFall() {
    // Minecraft-style: crouching prevents falling off block edges
    return true;
  }
  
  @Override
  public void onEnter(PlayerMovementContext context) {
    // Could lower camera height here for visual effect
  }
  
  @Override
  public void onExit(PlayerMovementContext context) {
    // Could restore camera height
  }
}
