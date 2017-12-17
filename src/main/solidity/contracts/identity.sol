pragma solidity ^0.4.17;

/// @title Vote based addition of an identity
contract IdentityRegistry {
	
	address registryOwner;
	
	uint requiredVotes;
	
	struct Identity {
		string test;
		address identityAddress; // Each identity has an address, so it can add signatures
		bytes identityToken; // Externally verifiable identity (opaque data within the DLT)
		address[] signatures; // Signatures attesting to the identity
		bool trusted; // Whether the identity has crossed the trust threshold of signatures
	}
	
	// The identities added to the registry
	mapping(address => Identity) public identities;
	
	function IdentityRegistry(uint _requiredVotes) public {
		registryOwner = msg.sender;
		if (_requiredVotes == 0) {
			_requiredVotes = 3;
		}
		requiredVotes = _requiredVotes;
	}
	
	function getRegistryOwner() public view returns(address) {
		return registryOwner;
	}
	
	function addInitialTrustedAddress(address identityAddress, bytes identityToken) public {
		assert(msg.sender == registryOwner);
		var identity = identities[identityAddress];
		assert(identity.signatures.length == 0);
		identity.identityAddress = identityAddress;
		identity.identityToken = identityToken;
		identity.trusted = true;
	}
	
	function voteForIdentity(address identityAddress, bytes identityToken) public {
		// Check that the sender is already trusted
		var senderIdentity = identities[msg.sender];
		assert(senderIdentity.trusted);
		
		var identity = identities[identityAddress];
		if (identity.signatures.length == 0) {
			// We're the first signature - initialise the identity
			identity.identityAddress = identityAddress;
			identity.identityToken = identityToken;
		} else {
			// Check the identity matches
			assert(keccak256(identity.identityToken) == keccak256(identityToken));
			assert(identity.identityAddress == identityAddress);			
		}
				
		// Check there is not already a signature
		for (uint i = 0; i < identity.signatures.length; i++) {
			assert(identity.signatures[i] != msg.sender);
		}
		
		// Add the signature
		identity.signatures.push(msg.sender);
		
		// Check if we've just become trusted
		identity.trusted = identity.signatures.length >= requiredVotes;
	}

   	function isTrusted(address identityAddress) public view returns(bool) {
   		return identities[identityAddress].trusted;
   	}

   	function getSignatureCount(address identityAddress) public view returns(uint256) {
   		return identities[identityAddress].signatures.length;
   	}
}
