/**
 * Copyright 2014 Coinport Inc. All Rights Reserved.
 * Author: c@coinport.com (Chao Ma)
 */

'use strict'

var Assert                    = require('assert'),
    MockRpc                   = require('./mock_rpc').MockRpc,
    MockRedis                 = require('./mock_redis').MockRedis,
    CryptoProxy               = require('../../../../src/coinport/bitway/crypto/crypto_proxy').CryptoProxy,
    DataTypes                 = require('../../../../gen-nodejs/data_types'),
    MessageTypes              = require('../../../../gen-nodejs/message_types'),
    BitwayMessage             = MessageTypes.BitwayMessage,
    GenerateAddresses         = MessageTypes.GenerateAddresses,
    GenerateAddressesResult   = MessageTypes.GenerateAddressesResult,
    CryptoCurrencyAddressType = DataTypes.CryptoCurrencyAddressType,
    ErrorCode                 = DataTypes.ErrorCode,
    Currency                  = DataTypes.Currency;

describe('crypto proxy', function() {
    describe('generateUserAddress', function() {
        it('get 4 addresses', function(done) {
            var cryptoProxy = new CryptoProxy(Currency.BTC, {
                cryptoRpc: new MockRpc({fail: 'none'}),
                minConfirm: 1,
                redis: 'noredis'
            });
            cryptoProxy.generateUserAddress(new GenerateAddresses({num: 4}), function(response) {
                var expectRes = new BitwayMessage({currency: Currency.BTC, generateAddressResponse:
                    new GenerateAddressesResult({error: ErrorCode.OK, addresses: ['addr', 'addr', 'addr', 'addr'],
                        addressType: CryptoCurrencyAddressType.UNUSED})})
                Assert.deepEqual(response, expectRes);
                done();
            });
        });
        it('rpc error occur while generating user addresses', function(done) {
            var cryptoProxy = new CryptoProxy(Currency.BTC, {
                cryptoRpc: new MockRpc({fail: 'all'}),
                minConfirm: 1,
                redis: 'noredis'
            });
            cryptoProxy.generateUserAddress(new GenerateAddresses({num: 4}), function(response) {
                var expectRes = new BitwayMessage({currency: Currency.BTC, generateAddressResponse:
                    new GenerateAddressesResult({error: ErrorCode.RPC_ERROR, addresses: null,
                        addressType: CryptoCurrencyAddressType.UNUSED})});
                Assert.deepEqual(response, expectRes);
                done();
            });
        });

        it('partial fail while generating user addresses', function(done) {
            var cryptoProxy = new CryptoProxy(Currency.BTC, {
                cryptoRpc: new MockRpc({fail: 'partial'}),
                minConfirm: 1,
                redis: 'noredis'
            });
            cryptoProxy.generateUserAddress(new GenerateAddresses({num: 4}), function(response) {
                var expectRes = new BitwayMessage({currency: Currency.BTC, generateAddressResponse:
                    new GenerateAddressesResult({error: ErrorCode.RPC_ERROR, addresses: null,
                        addressType: CryptoCurrencyAddressType.UNUSED})});
                Assert.deepEqual(response, expectRes);
                done();
            });
        });
    });

    describe('getNewCCTXsSinceLatest_', function() {
        it('get new cctxs from latest height', function(done) {
            var cryptoProxy = new CryptoProxy(Currency.BTC, {
                cryptoRpc: new MockRpc({fail: 'partial', blockCount: 244498}),
                minConfirm: 1,
                redis: new MockRedis()
            });
            cryptoProxy.getNewCCTXsSinceLatest_(function(error, cctxs) {
                Assert.deepEqual(cctxs, [{
                    "sigId": "aeefac23a12130754be0512ab8986ef740f8e381dc4cc5a606e6a3630f7d4033",
                    "txid": "8debdd1691d1bff1e0b9f27cbf4958c9b7578e2bd0b50334a2bcc7060217e7a7",
                    "ids": null,
                    "inputs": [
                    {
                        "address": "n1YiTZ9SczJM5ZpRgBmRP2B5Gax7JHptAa",
                        "amount": 4.6498,
                        "internalAmount": null, "userId": null
                    }],
                    "outputs": [
                    {
                        "address": "mgen5m7yqkXckzqgug1cZFbtZz8cRZApqY",
                        "amount": 4.6178,
                        "internalAmount": null, "userId": null
                    },
                    {
                        "address": "mhfF1SYE8juppzWTFX2T7UBuSWT13yX5Jk",
                        "amount": 0.032,
                        "internalAmount": null, "userId":null
                    }],
                    "prevBlock": null,
                    "includedBlock": null,
                    "txType": null,
                    "status": 2,
                    "timestamp":null
                }]);
                cryptoProxy.getNewCCTXsSinceLatest_(function(error, cctxs) {
                    Assert.deepEqual(cctxs, []);
                    done();
                });
            });
        });
    });
});
