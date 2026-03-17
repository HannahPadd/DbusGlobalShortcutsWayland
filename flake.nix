{
  description = "Simple Java 17 Flake";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-parts.url = "github:hercules-ci/flake-parts";
  };

  outputs = inputs@{ self, nixpkgs, flake-parts, ... }:
    flake-parts.lib.mkFlake { inherit inputs; } {
      systems = [ "x86_64-linux" ];

      perSystem = { pkgs, ... }:
        let
          runtimeLibs = pkgs: (with pkgs; [
            jdk17
          ]);

          javaShell = pkgs.buildFHSEnv {
            name = "Java-17 env";
            targetPkgs = runtimeLibs;
            profile = ''
              export JAVA_HOME=${pkgs.jdk17}
              export PATH="${pkgs.jdk17}/bin:$PATH"
            '';
            runScript = "zsh";
          };
        in
        {
          devShells.default = javaShell.env;
        };
    };
}
