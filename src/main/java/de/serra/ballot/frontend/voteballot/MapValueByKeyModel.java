package de.serra.ballot.frontend.voteballot;

import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.util.Map;

class MapValueByKeyModel<K extends Serializable, V> implements IModel<V> {
	private final Map<K, V> map;
	private final K key;

	MapValueByKeyModel(Map<K, V> map, K key) {
		this.map = map;
		this.key = key;
	}

	@Override
	public V getObject() {
		return map.get(key);
	}

	@Override
	public void setObject(V object) {
		map.put(key, object);
	}
}
