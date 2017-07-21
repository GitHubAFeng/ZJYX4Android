package com.ihangjing.common;

public class EasyEatException {
	public static class NetworkException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public NetworkException(String paramString, Throwable paramThrowable) {
			super(paramThrowable);
		}
	}

	public static class SdcardException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SdcardException(String paramString, Throwable paramThrowable) {
			super(paramThrowable);
		}
	}

	public static class UnknownException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UnknownException(String paramString, Throwable paramThrowable) {
			super(paramThrowable);
		}
	}
}