# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
# SRC_URI = "git://git@github.com/cu-ecen-aeld/<your assignments repo>;protocol=ssh;branch=master"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-calvarado2004;protocol=ssh;branch=main;submodules=1"

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
FILES:${PN} += "${bindir}/aesdsocket"
# TODO: customize these as necessary for any libraries you need for your application
# (and remove comment)
LDFLAGS = "-Wl,-O1 -Wl,--as-needed -Wl,--hash-style=gnu -Wl,-z,relro,-z,now"

TARGET_LDFLAGS += "${LDFLAGS} -pthread -lrt"

inherit update-rc.d
FILES:${PN} += "${bindir}/aesdsocket"
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "aesdsocket-start-stop"

do_configure () {
	:
}

do_compile () {
    # Compile the object files
    oe_runmake CC="${CC}" -C ${S} all
    
    # Manually link the binaries with the appropriate LDFLAGS
    ${CC} ${TARGET_LDFLAGS} -o ${S}/aesdsocket ${S}/aesdsocket.o -pthread -lrt
}

do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb

    install -d ${D}${bindir}
    install -m 0775 ${S}/aesdsocket ${D}${bindir}/

    install -d ${D}${sysconfdir}/init.d
    install -m 0775 ${S}/aesdsocket-start-stop ${D}${sysconfdir}/init.d
}
