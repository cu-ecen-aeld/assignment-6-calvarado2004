# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
# SRC_URI = "git://git@github.com/cu-ecen-aeld/<your assignments repo>;protocol=ssh;branch=master"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-calvarado2004;protocol=ssh;branch=main"

PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
#SRCREV = "f99b82a5d4cb2a22810104f89d4126f52f4dfaba"
SRCREV = "fac3fb09c7e549db30cd0e017f024ead78533293"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
S = "${WORKDIR}/git/server"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
#FILES:${PN} += "${bindir}/aesdsocket"
# TODO: customize these as necessary for any libraries you need for your application
# (and remove comment)
#TARGET_LDFLAGS += "-pthread -lrt"

do_configure () {
	:
}

do_compile() {
    oe_runmake CC="${CC}" -C ${S}/finder-app all
    oe_runmake CC="${CC}" -C ${S}/server all
}

do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb
    install -d ${D}/etc/finder-app/conf/
    install -m 0755 ${S}/conf/* ${D}/etc/finder-app/conf/
    install -d ${D}${bindir}
    install -m 0755 ${S}/assignment-autotest/test/assignment4/* ${D}${bindir}/
    install -D -m 755 ${S}/finder-app/finder.sh ${D}${bindir}/finder.sh
    install -D -m 755 ${S}/finder-app/finder-test.sh ${D}${bindir}/tester.sh
    install -D -m 755 ${S}/finder-app/finder-test.sh ${D}${bindir}/finder-test.sh
    install -D -m 755 ${S}/finder-app/writer ${D}${bindir}/writer
    install -D -m 755 ${S}/server/aesdsocket ${D}${bindir}/aesdsocket
    install -D -m 755 ${S}/server/aesdsocket-start-stop ${D}${sysconfdir}/init.d/S99aesdsocket
}

FILES:${PN} += "${bindir}/*"
