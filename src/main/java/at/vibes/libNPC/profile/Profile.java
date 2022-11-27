package at.vibes.libNPC.profile;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Profile implements Cloneable {
    private static final Random RANDOM = new Random();

    private static final ThreadLocal<Gson> GSON = ThreadLocal
            .withInitial(() -> new GsonBuilder().serializeNulls().create());

    private static final String UUID_REQUEST_URL = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String TEXTURES_REQUEST_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=%b";

    private static final Pattern UNIQUE_ID_PATTERN = Pattern
            .compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
    private static final Type PROPERTY_LIST_TYPE = TypeToken
            .getParameterized(Set.class, Property.class).getType();

    private String name = new UUID(RANDOM.nextLong(), 0).toString().substring(0, 16);
    private UUID uniqueId;
    private Collection<Property> properties;

    public Profile(@NotNull UUID uniqueId) {
        this(uniqueId, null);
    }

    public Profile(@NotNull UUID uniqueId, Collection<Property> properties) {
        this(uniqueId, null, properties);
    }

    public Profile(@NotNull String name) {
        this(name, null);
    }

    public Profile(@NotNull String name, Collection<Property> properties) {
        this(null, name, properties);
    }

    public Profile(UUID uniqueId, String name, Collection<Property> properties) {
        Preconditions
                .checkArgument(uniqueId != null, "Either name or uniqueId must be given!");

        this.uniqueId = uniqueId;
        this.properties = properties;
    }

    public boolean isComplete() {
        return this.uniqueId != null && this.name != null;
    }

    public boolean hasTextures() {
        return this.getProperty("textures").isPresent();
    }

    public boolean hasProperties() {
        return this.properties != null && !this.properties.isEmpty();
    }

    public boolean complete() {
        return this.complete(true);
    }

    public boolean complete(boolean propertiesAndName) {
        if (this.isComplete() && this.hasProperties()) {
            return true;
        }

        if (this.uniqueId == null) {
            JsonElement identifierElement = this.makeRequest(String.format(UUID_REQUEST_URL, this.name));
            if (identifierElement == null || !identifierElement.isJsonObject()) {
                return false;
            }

            JsonObject jsonObject = identifierElement.getAsJsonObject();
            if (jsonObject.has("id")) {
                this.uniqueId = UUID.fromString(
                        UNIQUE_ID_PATTERN.matcher(jsonObject.get("id").getAsString())
                                .replaceAll("$1-$2-$3-$4-$5"));
            } else {
                return false;
            }
        }

        if ((this.name == null || this.properties == null) && propertiesAndName) {
            JsonElement profileElement = this.makeRequest(
                    String.format(TEXTURES_REQUEST_URL, this.uniqueId.toString().replace("-", ""), false));
            if (profileElement == null || !profileElement.isJsonObject()) {
                return false;
            }

            JsonObject object = profileElement.getAsJsonObject();
            if (object.has("name") && object.has("properties")) {
                this.name = this.name == null ? object.get("name").getAsString() : this.name;
                this.getProperties()
                        .addAll(GSON.get().fromJson(object.get("properties"), PROPERTY_LIST_TYPE));
            } else {
                return false;
            }
        }

        return true;
    }

    protected @Nullable JsonElement makeRequest(@NotNull String apiUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setUseCaches(true);
            connection.connect();

            if (connection.getResponseCode() == 200) {
                try (Reader reader = new InputStreamReader(connection.getInputStream(),
                        StandardCharsets.UTF_8)) {
                    return JsonParser.parseReader(reader);
                }
            }
            return null;
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public boolean hasUniqueId() {
        return this.uniqueId != null;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @NotNull
    public Profile setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
        return this;
    }

    public boolean hasName() {
        return this.name != null;
    }

    public String getName() {
        return this.name;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @NotNull
    public Profile setName(String name) {
        this.name = name;
        return this;
    }

    @NotNull
    public Collection<Property> getProperties() {
        if (this.properties == null) {
            this.properties = ConcurrentHashMap.newKeySet();
        }
        return this.properties;
    }

    public void setProperties(Collection<Property> properties) {
        this.properties = properties;
    }

    @NotNull
    public Profile setProperty(@NotNull Property property) {
        this.getProperties().add(property);
        return this;
    }

    public @NotNull Optional<Property> getProperty(@NotNull String name) {
        return this.getProperties().stream().filter(property -> property.getName().equals(name))
                .findFirst();
    }

    public void clearProperties() {
        this.getProperties().clear();
    }

    @NotNull
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public Collection<WrappedSignedProperty> getWrappedProperties() {
        return this.getProperties().stream().map(Property::asWrapped).collect(Collectors.toList());
    }

    @NotNull
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public WrappedGameProfile asWrapped() {
        return this.asWrapped(true);
    }

    @NotNull
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public WrappedGameProfile asWrapped(boolean withProperties) {
        WrappedGameProfile profile = new WrappedGameProfile(this.getUniqueId(), this.getName());

        if (withProperties) {
            this.getProperties().forEach(
                    property -> profile.getProperties().put(property.getName(), property.asWrapped()));
        }

        return profile;
    }

    @Override
    public Profile clone() {
        try {
            return (Profile) super.clone();
        } catch (CloneNotSupportedException exception) {
            return new Profile(this.uniqueId, this.name,
                    this.properties == null ? null : new HashSet<>(this.properties));
        }
    }

    public static class Property {
        private final String name;
        private final String value;
        private final String signature;

        public Property(@NotNull String name, @NotNull String value, @Nullable String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        @NotNull
        public String getName() {
            return this.name;
        }

        @NotNull
        public String getValue() {
            return this.value;
        }

        @Nullable
        public String getSignature() {
            return this.signature;
        }

        public boolean isSigned() {
            return this.signature != null;
        }

        @NotNull
        @Deprecated
        @ApiStatus.Internal
        public WrappedSignedProperty asWrapped() {
            return new WrappedSignedProperty(this.getName(), this.getValue(), this.getSignature());
        }
    }
}