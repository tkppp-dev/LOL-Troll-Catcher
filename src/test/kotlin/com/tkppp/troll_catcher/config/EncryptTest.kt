package com.tkppp.troll_catcher.config

import org.assertj.core.api.Assertions.assertThat
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EncryptTest(
    @Autowired private val encryptor: PooledPBEStringEncryptor
) {
    @Test
    @DisplayName("설정파일 암호화 및 테스트")
    fun configEncrypt(){
        // given
        val content = ""

        // when
        val encryptResult = encryptor.encrypt(content)
        val decryptResult = encryptor.decrypt(encryptResult)

        // then
        assertThat(decryptResult).isEqualTo(content)
        println("Encrypt Success")
        println("result : ${encryptor.encrypt(content)}")
    }
}