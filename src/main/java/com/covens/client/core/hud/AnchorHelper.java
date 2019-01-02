package com.covens.client.core.hud;

public abstract class AnchorHelper {

	public abstract double getData(double x, int scaledSize, int componentSize);

	public abstract double getPixel(double x, int scaledSize, int componentSize);

	public static class AbsoluteStartHelper extends AnchorHelper {

		@Override
		public double getData(double x, int scaledSize, int componentSize) {
			return x;
		}

		@Override
		public double getPixel(double x, int scaledSize, int componentSize) {
			return x;
		}

	}

	public static class AbsoluteEndHelper extends AnchorHelper {

		@Override
		public double getData(double x, int scaledSize, int componentSize) {
			return scaledSize - componentSize - x;
		}

		@Override
		public double getPixel(double x, int scaledSize, int componentSize) {
			return scaledSize - componentSize - x;
		}

	}

	public static class AbsoluteCenterHelper extends AnchorHelper {

		@Override
		public double getData(double x, int scaledSize, int componentSize) {
			return x - ((scaledSize - componentSize) / 2);
		}

		@Override
		public double getPixel(double x, int scaledSize, int componentSize) {
			return ((scaledSize - componentSize) / 2) + x;
		}

	}

	public static class RelativeVersion extends AnchorHelper {

		private AnchorHelper used;

		public RelativeVersion(AnchorHelper helper) {
			this.used = helper;
		}

		@Override
		public double getData(double x, int scaledSize, int componentSize) {
			return this.used.getData(x, scaledSize, componentSize) / scaledSize;
		}

		@Override
		public double getPixel(double x, int scaledSize, int componentSize) {
			return this.used.getPixel(x * scaledSize, scaledSize, componentSize);
		}

	}

}
