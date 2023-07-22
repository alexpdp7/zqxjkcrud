#!/usr/bin/env python3
import re
import shlex
import subprocess


def _run(cmd, check=True, cwd=None, **kwargs):
    print(f"> ({cwd}) $ {shlex.join(cmd)}", flush=True)
    return subprocess.run(cmd, **kwargs, check=check, cwd=cwd)


def describe(long):
    long_args = ["--long"] if long else []
    description = _run(["git", "describe", "--all", "--always"] + long_args, encoding="utf8", stdout=subprocess.PIPE).stdout
    assert description.startswith("heads/"), f"unexpected description {description}"
    return description[len("heads/"):-1]  # remove heads/ and trailing newline


def is_dirty():
    returncode = _run(["git", "diff-index", "--quiet", "HEAD", "--"], check=False).returncode
    assert returncode in (0,1), f"returncode is {returncode}"
    return returncode == 1


def build_images_and_push():
    tags = ["dirty"] if is_dirty() else [describe(False), describe(True)]
    for tag in tags:
        tag = re.sub(r"\W", "_", tag)
        quay_expires_after = "never" if not is_dirty() and tag == "master" else "3d"
        _run(["docker", "build", ".", "--build-arg", f"QUAY_EXPIRES_AFTER={quay_expires_after}", "-t", f"quay.io/alexpdp7/zqxjkcrud:{tag}"])
        assert tag != "dirty", "attempting to push dirty image"
        _run(["docker", "push", f"quay.io/alexpdp7/zqxjkcrud:{tag}"])
    return tags
