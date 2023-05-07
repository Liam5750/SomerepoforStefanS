package ravenNPlus.client.utils;

public class Timer {

   public static long lastMS = System.currentTimeMillis();
   private final float updates;
   private float cached;
   private long last;

   public Timer(float updates) {
      this.updates = updates;
   }

   public static boolean hasTimeElapsed(long time) {
      return System.currentTimeMillis() - lastMS > time;
   }

   public long getTime() {
      return System.currentTimeMillis() - lastMS;
   }

   public void setTime(long time) {
      lastMS = time;
   }

   public float getValueFloat(float begin, float end, int type) {
      if (this.cached == end) {
         return this.cached;
      } else {
         float t = (float)(System.currentTimeMillis() - this.last) / this.updates;
         switch(type) {
         case 1:
            t = t < 0.5F ? 4.0F * t * t * t : (t - 1.0F) * (2.0F * t - 2.0F) * (2.0F * t - 2.0F) + 1.0F;
            break;
         case 2:
            t = (float)(1.0D - Math.pow(1.0F - t, 5.0D));
            break;
         case 3:
            t = this.bounce(t);
         }

         float value = begin + t * (end - begin);
         if (end < value) {
            value = end;
         }

         if (value == end) {
            this.cached = value;
         }

         return value;
      }
   }

   public int getValueInt(int begin, int end, int type) {
      return Math.round(this.getValueFloat((float)begin, (float)end, type));
   }

   public void start() {
      this.cached = 0.0F;
      this.last = System.currentTimeMillis();
   }

   public void setLast(long last) {
      this.last = last;
   }

   public long convertToNS(final long time) {
      return time / 10000000L;
   }

   private float bounce(float t) {
      double i2 = 7.5625D;
      double i3 = 2.75D;
      if ((double) t < 1.0D / i3) {
         return (float) (i2 * (double) t * (double) t);
      } else if ((double) t < 2.0D / i3) {
         return (float) (i2 * (double) (t = (float) ((double) t - 1.5D / i3)) * (double) t + 0.75D);
      } else if ((double) t < 2.5D / i3) {
         return (float) (i2 * (double) (t = (float) ((double) t - 2.25D / i3)) * (double) t + 0.9375D);
      } else {
         return (float) (i2 * (double) (t = (float) ((double) t - 2.625D / i3)) * (double) t + 0.984375D);
      }
   }

   public static void reset() {
      lastMS = System.currentTimeMillis();
   }

   public static boolean hasTimeElapsed(long milliseconds, boolean reset) {
      if (System.currentTimeMillis() - lastMS > milliseconds) {

         if (reset)
            reset();

         return true;
      }

      return false;
   }

   public static float fadeIn(float fadePerc) {
      long currentTime = System.currentTimeMillis();
      float delta = (currentTime-lastMS) / 1000f;

      lastMS = System.currentTimeMillis();

      // u can use the delta time now
      fadePerc += (delta*0.5);
      if (fadePerc > 0.6) {
         fadePerc = 0.6f;
      }

      return fadePerc;
   }

   public static float fadeOut(float fadePercentage) {
      long currentTime = System.currentTimeMillis();
      float delta = (currentTime-lastMS) / 1000f;
      lastMS = System.currentTimeMillis();

      // u can use the delta time now
      fadePercentage -= (delta*2);
      if (fadePercentage < 0.6) {
         fadePercentage = 0.6f;
      }

      return fadePercentage;
   }

}