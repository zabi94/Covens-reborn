package com.covens.common.core.util;

import java.util.function.Supplier;

public class CachedSupplier<T> implements Supplier<T> {

	private T cachedObject = null;
	private Supplier<T> supplier;

	public CachedSupplier(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	@Override
	public T get() {
		if (this.cachedObject == null) {
			this.cachedObject = this.supplier.get();
		}
		return this.cachedObject;
	}

	public void invalidateCache() {
		this.cachedObject = null;
	}

}
