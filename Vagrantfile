Vagrant.configure(2) do |config|
    config.vm.box = "ubuntu/trusty64"
    config.ssh.forward_x11 = true
    config.vm.provision :shell, path: "bootstrap.sh"
    config.vm.provider "virtualbox" do |vb|
        vb.customize ["usbfilter", "add", "0",
            "--target", :id, 
            "--name", "android",
            "--manufacturer", "HTC",
            "--product", "Android Phone"]
    end
end
