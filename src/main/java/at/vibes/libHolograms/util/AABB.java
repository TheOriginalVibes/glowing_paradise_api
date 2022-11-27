package at.vibes.libHolograms.util;

import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class AABB {

    private Vec3D max;
    private Vec3D min;

    public AABB(Vec3D min, Vec3D max) {
        this.min = min;
        this.max = max;
    }

    public AABB(Location block) {
        this(Vec3D.fromLocation(block), Vec3D.fromLocation(block).add(Vec3D.UNIT_MAX));
    }

    public void translate(Vec3D vec) {
        this.min = this.min.add(vec);
        this.max = this.max.add(vec);
    }

    public Vec3D intersectsRay(Ray3D ray, float minDist, float maxDist) {
        Vec3D invDir = new Vec3D(1f / ray.dir.x, 1f / ray.dir.y, 1f / ray.dir.z);

        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        boolean signDirZ = invDir.z < 0;

        Vec3D bbox = signDirX ? max : min;
        double tmin = (bbox.x - ray.x) * invDir.x;
        bbox = signDirX ? min : max;
        double tmax = (bbox.x - ray.x) * invDir.x;
        bbox = signDirY ? max : min;
        double tymin = (bbox.y - ray.y) * invDir.y;
        bbox = signDirY ? min : max;
        double tymax = (bbox.y - ray.y) * invDir.y;

        if ((tmin > tymax) || (tymin > tmax)) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }

        bbox = signDirZ ? max : min;
        double tzmin = (bbox.z - ray.z) * invDir.z;
        bbox = signDirZ ? min : max;
        double tzmax = (bbox.z - ray.z) * invDir.z;

        if ((tmin > tzmax) || (tzmin > tmax)) {
            return null;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        if ((tmin < maxDist) && (tmax > minDist)) {
            return ray.getPointAtDistance(tmin);
        }
        return null;
    }

    public static class Vec3D {
        public static final Vec3D UNIT_MAX = new Vec3D(1, 1, 1);

        public final double x;
        public final double y;
        public final double z;

        public Vec3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vec3D(Vec3D v) {
            this.x = v.x;
            this.y = v.y;
            this.z = v.z;
        }

        public static Vec3D fromLocation(Location loc) {
            return new Vec3D(loc.getX(), loc.getY(), loc.getZ());
        }

        public static Vec3D fromVector(Vector v) {
            return new Vec3D(v.getX(), v.getY(), v.getZ());
        }

        public final Vec3D add(Vec3D v) {
            return new Vec3D(x + v.x, y + v.y, z + v.z);
        }

        public Vec3D scale(double s) {
            return new Vec3D(x * s, y * s, z * s);
        }

        public Vec3D normalize() {
            double mag = Math.sqrt(x * x + y * y + z * z);

            if (mag > 0) {
                return scale(1.0 / mag);
            }
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Vec3D) {
                Vec3D v = (Vec3D) obj;
                return x == v.x && y == v.y && z == v.z;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return String.format("{x: %g, y: %g, z: %g}", x, y, z);
        }
    }

    public static class Ray3D extends Vec3D {

        public final Vec3D dir;

        public Ray3D(Vec3D origin, Vec3D direction) {
            super(origin);
            dir = direction.normalize();
        }

        public Ray3D(Location loc) {
            this(Vec3D.fromLocation(loc), Vec3D.fromVector(loc.getDirection()));
        }

        public Vec3D getDirection() {
            return dir;
        }

        public Vec3D getPointAtDistance(double dist) {
            return add(dir.scale(dist));
        }

        @Override
        public String toString() {
            return "origin: " + super.toString() + " dir: " + dir;
        }
    }

}