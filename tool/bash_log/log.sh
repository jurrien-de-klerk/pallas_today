#!/usr/bin/env bash
# log.sh — Logging library. Source this file to use the log functions.
#
# Usage:
#   source "$(dirname "${BASH_SOURCE[0]}")/log.sh"
#
#   log::debug "Starting process"
#   log::info  "Server started on port 8080"
#   log::warn  "Config file not found, using defaults"
#   log::error "Failed to connect to database"
#
# Log level filtering:
#   Set LOG_LEVEL to limit output. Valid values (in ascending order):
#   DEBUG, INFO, WARN, ERROR. Default: INFO.
#
#   LOG_LEVEL=DEBUG source log.sh   # show all levels
#   LOG_LEVEL=WARN  source log.sh   # show only WARN and ERROR

: "${LOG_LEVEL:=INFO}"

# --- Internal helpers ---------------------------------------------------------

_log::_level_value() {
	case "$1" in
	DEBUG) echo 0 ;;
	INFO) echo 1 ;;
	WARN) echo 2 ;;
	ERROR) echo 3 ;;
	*) echo 1 ;;
	esac
}

_log::_write() {
	local level="$1"
	local message="$2"
	local timestamp
	timestamp="$(date -u '+%Y-%m-%dT%H:%M:%SZ')"

	if [[ "$(_log::_level_value "$level")" -ge "$(_log::_level_value "$LOG_LEVEL")" ]]; then
		local color reset
		reset="\033[0m"
		case "$level" in
		DEBUG) color="\033[0;36m" ;; # cyan
		INFO) color="\033[0;32m" ;;  # green
		WARN) color="\033[0;33m" ;;  # yellow
		ERROR) color="\033[0;31m" ;; # red
		esac

		local output
		if [[ -t 2 ]]; then
			output="${color}${timestamp} [${level}]${reset} ${message}"
		else
			output="${timestamp} [${level}] ${message}"
		fi

		if [[ "$level" == "ERROR" ]]; then
			echo -e "$output" >&2
		else
			echo -e "$output"
		fi
	fi
}

# --- Public functions ---------------------------------------------------------

log::debug() { _log::_write "DEBUG" "$*"; }
log::info() { _log::_write "INFO" "$*"; }
log::warn() { _log::_write "WARN" "$*"; }
log::error() { _log::_write "ERROR" "$*"; }
