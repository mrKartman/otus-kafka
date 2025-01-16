package ru.cherkashin.trafficparticipantdispatcher.serde;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.WindowStore;

import java.util.ArrayList;
import java.util.List;

public class AppMaterialized {

    public static Materialized<Integer, List<Integer>, WindowStore<Bytes, byte[]>> listInteger(String storeName) {
        Serde<List<Integer>> listSerde = Serdes.ListSerde(ArrayList.class, Serdes.Integer());
        return Materialized.<Integer, List<Integer>, WindowStore<Bytes, byte[]>>as(storeName)
                .withKeySerde(Serdes.Integer()).withValueSerde(listSerde);
    }
}
