#!/usr/bin/env sh
# Lightweight delegator to system gradle if wrapper jar is absent.
set -eu
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
fi
if [ -x "./gradle/wrapper/gradle-wrapper.jar" ]; then
  echo "Use standard wrapper script when gradle-wrapper.jar exists."
fi
echo "Gradle is required. Install gradle or provide wrapper jar." >&2
exit 1
