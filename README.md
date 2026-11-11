mvn -pl springframework -am -Pnative native:compile -DskipTests

./springframework/target/springframework

# x86_64 (amd64)
docker build --platform linux/amd64 -t login .

# arm64
docker build --platform linux/arm64 -t login .