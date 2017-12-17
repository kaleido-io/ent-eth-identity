var IdentityRegistry = artifacts.require("IdentityRegistry");

contract('IdentityRegistry', function(accounts) {
  it("should allow bootstrapping initial identities", function() {
    var instance;
    return IdentityRegistry.deployed().then(function(_instance) {
      instance = _instance;

      console.log("Check the registry owner");
      return instance.getRegistryOwner.call();
    }).then(function(registryOwner) {
      console.log('Accounts: ' + require('util').format(accounts));
      assert.equal(registryOwner, accounts[0])

      console.log("Bootstrap the owner as a trusted identity");
      return instance.addInitialTrustedAddress(accounts[0], '0xaaaa');
    }).then(function() {

      console.log("Bootstrap account1 as a trusted identity");
      return instance.addInitialTrustedAddress(accounts[1], '0xbbbb');
    }).then(function() {

      console.log("Bootstrap account2 as a trusted identity");
      return instance.addInitialTrustedAddress(accounts[2], '0xcccc');
    }).then(function() {

      console.log("Check owner is now trusted");
      return instance.isTrusted.call(accounts[0]);
    }).then(function(isTrusted) {
      assert.equal(isTrusted, true);

      console.log("Check account1 is now trusted");
      return instance.isTrusted.call(accounts[1]);
    }).then(function(isTrusted) {
      assert.equal(isTrusted, true);

      console.log("Check account2 is now trusted");
      return instance.isTrusted.call(accounts[2]);
    }).then(function(isTrusted) {
      assert.equal(isTrusted, true);

      console.log("Check account3 is NOT trusted yet");
      return instance.isTrusted.call(accounts[3]);
    }).then(function(isTrusted) {
      assert.equal(isTrusted, false);

      console.log("Add a vote from owner");
      return instance.voteForIdentity(accounts[3], '0xdddd');
    }).then(function() {

      console.log("Add a vote from account1");
      return instance.voteForIdentity(accounts[3], '0xdddd', {from: accounts[1]});
    }).then(function() {

      console.log("Attempt a vote from account3 itself - should be rejected");
      return instance.voteForIdentity(accounts[3], '0xdddd', {from: accounts[3]});
    }).then(function() {

      console.log("Attempt a vote with a different token - should be rejected");
      return instance.voteForIdentity(accounts[3], '0xffff', {from: accounts[2]});
    }).then(function() {

      console.log("Check account3 is still NOT trusted yet");
      return instance.isTrusted.call(accounts[3]);
    }).then(function(isTrusted) {
      assert.equal(isTrusted, false);

      console.log("Add a vote from account2");
      return instance.voteForIdentity(accounts[3], '0xdddd', {from: accounts[2]});
    }).then(function() {

      console.log("Should now have 3 votes");
      return instance.getSignatureCount.call(accounts[3]);
    }).then(function(signatureCount) {
      assert.equal(signatureCount.toNumber(), 3);

      console.log("Should now be trusted");
      return instance.isTrusted.call(accounts[3]);
    }).then(function(isTrusted) {
      assert.equal(isTrusted, true);

    });
  });
});
