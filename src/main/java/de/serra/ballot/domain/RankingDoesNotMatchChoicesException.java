package de.serra.ballot.domain;

public class RankingDoesNotMatchChoicesException extends RuntimeException {
	public RankingDoesNotMatchChoicesException() {
	}

	public RankingDoesNotMatchChoicesException(String message) {
		super(message);
	}

	public RankingDoesNotMatchChoicesException(Throwable cause) {
		super(cause);
	}

	public RankingDoesNotMatchChoicesException(String message, Throwable cause) {
		super(message, cause);
	}

	public RankingDoesNotMatchChoicesException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
