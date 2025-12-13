package ethanjones.cubes.input.movement;

/**
 * State Pattern - Concrete State
 * 
 * Sprinting state - fast ground movement
 * Speed: 7.0 blocks/second (faster than walking, slower than flying)
 * 
 * Transitions:
 * - Sprint key released → WalkingState
 * - Double jump → FlyingState
 * - In air → WalkingState (sprint only works on ground)
 */
public class SprintingState extends MovementState {
  
  private static final float SPRINTING_SPEED = 7.0f;
  
  @Override
  public float getSpeed() {
    return SPRINTING_SPEED;
  }
  
  @Override
  public String getStateName() {
    return "Sprinting";
  }
  
  @Override
  public MovementState handleInput(PlayerMovementContext context, 
                                   boolean isSprintPressed, 
                                   boolean isCrouchPressed,
                                   boolean isOnGround) {
    // Sprint only works on ground - jumping interrupts sprint
    if (!isOnGround) {
      return context.getWalkingState();
    }
    
    // Sprint key released → back to walking
    if (!isSprintPressed) {
      return context.getWalkingState();
    }
    
    // Continue sprinting
    return this;
  }
  
  @Override
  public boolean canTransitionToFlying() {
    // Cannot transition to flying from sprint - must release Shift first
    return false;
  }
  
  @Override
  public void onEnter(PlayerMovementContext context) {
    // Could add sprint particles/effects
  }
  
  @Override
  public void onExit(PlayerMovementContext context) {
    // Could remove sprint particles
  }
}
