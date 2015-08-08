Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu/trusty64"
  config.ssh.forward_x11 = true
  config.vm.provision :shell, path: "bootstrap.sh"
end
