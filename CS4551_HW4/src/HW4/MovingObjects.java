package HW4;

public class MovingObjects {

	public void movingObjects(int num, int n){
		
		int ref = num - 2;
		
		Image target = new Image("Walk_0" + num + ".ppm");
   		Image reference = new Image("Walk_0" + ref + ".ppm");
   		
   		MotionCompensation mc = new MotionCompensation();
   		mc.motionCompensation(n, true, target, reference);
   		
   		
   		
	}
}
