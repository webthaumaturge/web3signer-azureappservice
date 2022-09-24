package tech.pegasys.web3signer.commandline.config;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import javax.annotation.Nonnull;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AllowListPublicKeysProperty extends AbstractList<String> {

    private final List<String> publicKeysAllowlist = new ArrayList<>();

    public AllowListPublicKeysProperty(){}

    @Override
    @Nonnull
    public Iterator<String> iterator() {
        if (publicKeysAllowlist.size() == 1 && publicKeysAllowlist.get(0).equals("none")) {
            return Collections.emptyIterator();
        } else {
            return publicKeysAllowlist.iterator();
        }
    }

    @Override
    public int size() {
        return publicKeysAllowlist.size();
    }

    @Override
    public boolean add(final String string) {
        return addAll(Collections.singleton(string));
    }

    @Override
    public String get(final int index) {
        return publicKeysAllowlist.get(index);
    }

    @Override
    public boolean addAll(final Collection<? extends String> collection) {
        final int initialSize = publicKeysAllowlist.size();
        for (final String string : collection) {
            if (Strings.isNullOrEmpty(string)) {
                throw new IllegalArgumentException("Public keys cannot be empty string or null string.");
            }
            for (final String s : Splitter.onPattern("\\s*,+\\s*").split(string)) {
                if ("all".equals(s)) {
                    publicKeysAllowlist.add("*");
                } else {
                    publicKeysAllowlist.add(s);
                }
            }
        }

        if (publicKeysAllowlist.contains("none")) {
            if (publicKeysAllowlist.size() > 1) {
                throw new IllegalArgumentException("Value 'none' can't be used with other public keys");
            }
        } else if (publicKeysAllowlist.contains("*")) {
            if (publicKeysAllowlist.size() > 1) {
                throw new IllegalArgumentException(
                        "Values '*' or 'all' can't be used with other public keys");
            }
        }

        return publicKeysAllowlist.size() != initialSize;
    }
}
