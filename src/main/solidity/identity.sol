pragma solidity ^0.4.11;

import ECRecovery from "erecovery.sol";

/// @title Vote based addition of an identity
contract IdentityRegistry {
	
	address registryOwner;
	
	uint requiredVotes;
	
	struct Identity {
		address identityAddress; // Each identity has an address, so it can add signatures
		bytes identityToken; // Externally verifiable identity (opaque data within the DLT)
		address[] signatures; // Signatures attesting to the identity
		bool trusted; // Whether the identity has crossed the trust threshold of signatures
	}
	
	// The identities added to the registry
	mapping(address => Identity) public identities;
	
	function IdentityRegistry(uint _requiredVotes) {
		registryOwner = msg.sender;
		requiredVotes = _requiredVotes;
	}
	
	function voteForIdentity(address identityAddress, bytes identityToken, bytes sig) {
		// Check that the sender is already trusted
		var senderIdentity = identities[msg.sender];
		assert(senderIdentity && senderIdentity.trusted);
		
		// Check that the sender has correctly signed the data
		var hash = keccak256(identityToken);
		assert(msg.sender == ECRecovery.recover(calculatedHash, sig));
		
		// Find or create an identity entry
		var identity = identities[identityAddress];
		if (!identity) {
			identity = Identity({
				identityAddress: identityAddress,
				identityToken: identityToken,
				signatures: address[]
				identity.trusted = false;
			});
			identities[identityAddress] = identity;
		}
		
		// Check there is not already a signature
		for (uint i = 0; i < identity.signatures.length; i++) {
			assert(identity.signatures[i] != msg.sender);
		}
		
		// Add the signature
		identity.signatures.push(msg.sender);
		
		// Check if we've just become trusted
		identity.trusted = identity.signatures.length > requiredVotes;
		
	}
   
}
