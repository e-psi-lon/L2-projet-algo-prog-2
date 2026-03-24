{
  description = "This file is useful because I'm the only person using NixOS";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/release-25.11";
  };

  outputs = { self, nixpkgs, ... }:
    let
      system = "x86_64-linux";
      pkgs = nixpkgs.legacyPackages.${system};
      project-jdk = pkgs.jdk25;
    in {
      devShells.${system}.default = pkgs.mkShell {
          shellHook = ''
              ln -sfn ${project-jdk}/lib/openjdk .jdk
              export JAVA_HOME=$PWD/.jdk
              
              if command -v git &>/dev/null && [ -d .git ]; then
                if ! git check-ignore -q .jdk 2>/dev/null; then
                  echo "Warning: .jdk is not git-ignored. Consider adding it to .gitignore."
                fi
              elif [ -f .gitignore ] && ! grep -q "^\.jdk" .gitignore; then
                echo "Warning: .jdk is not in .gitignore. Consider adding it to .gitignore."
              fi
          '';
          nativeBuildInputs = [
            project-jdk
          ];
        };
    };
}