var IdentityRegistry = artifacts.require("IdentityRegistry");

module.exports = function(deployer) {
  // Pass 42 to the contract as the first constructor parameter
  deployer.deploy(IdentityRegistry, 3);
};

